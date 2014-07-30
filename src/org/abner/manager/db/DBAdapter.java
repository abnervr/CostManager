package org.abner.manager.db;

import java.util.ArrayList;
import java.util.List;

import org.abner.manager.db.model.Table;
import org.abner.manager.db.model.ModelIterator;
import org.abner.manager.db.model.ModelProperties;
import org.abner.manager.model.Model;
import org.abner.manager.model.sms.Sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter {

    private final Context context;
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseHelper(context);
    }

    public DBAdapter open() throws SQLException {
        if (!isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
        return this;
    }

    public boolean isOpen() {
        return db != null && db.isOpen();
    }

    public void clear() {
        open();
        try {
            db.execSQL("delete from Movimento where id in (select movimentoId from Sms)");
            remove(Sms.class);
        } finally {
            close();
        }
    }

    public void drop() {
        for (String database : context.databaseList()) {
            context.deleteDatabase(database);
        }
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public void execute(String sql) {
        db.execSQL(sql);
    }

    public int remove(Model model) {
        return db.delete(dbHelper.getTableName(model.getClass()), "id = "
                        + model.getId(), null);
    }

    public int remove(Class<? extends Model> clazz) {
        return db.delete(dbHelper.getTableName(clazz), null, null);
    }

    public long insert(Model model) throws IllegalAccessException,
                    IllegalArgumentException {
        ContentValues initialValues = getContentValues(model);
        long id = db.insert(dbHelper.getTableName(model.getClass()), null,
                        initialValues);
        if (id >= 0) {
            model.setId(id);
        }
        return id != -1 ? 1 : 0;
    }

    public int update(Model model) throws IllegalAccessException,
                    IllegalArgumentException {
        return update(model, "id = " + model.getId(), null);
    }

    /**
     * Atualiza com base em um modelo, tome cuidado com tipos primitivos.
     * 
     */
    public int update(Model sample, String where, String[] whereArgs)
                    throws IllegalAccessException, IllegalArgumentException {
        return update(sample.getClass(), getContentValues(sample), where,
                        whereArgs);
    }

    public int update(Class<? extends Model> sample,
                    ContentValues contentValues, String where, String[] whereArgs)
                    throws IllegalAccessException, IllegalArgumentException {
        return db.update(dbHelper.getTableName(sample), contentValues, where,
                        whereArgs);
    }

    @SuppressWarnings("unchecked")
    private <M> ContentValues getContentValues(M model) {
        Table<M> modelHelper = new Table<M>((Class<M>) model.getClass());
        ContentValues initialValues = modelHelper.getContentValues(model);

        return initialValues;
    }

    public <T extends Model> T findById(Class<T> model, Long id) {

        if (id != null) {

            List<T> items = find(model, "id = " + id);

            if (items != null && !items.isEmpty()) {
                return items.get(0);
            }
        }

        return null;
    }

    public <T extends Model> List<T> find(Class<T> model) {
        return find(model, null);
    }

    public <T extends Model> List<T> find(Class<T> model, String where) {
        return find(model, where, null, null, null, null, null);
    }

    public <T extends Model> List<T> find(Class<T> model, String where, String[] selectionArgs, String groupBy, String having,
                    String orderBy, String limit) {
        List<String> columns = ModelProperties.getColumnNames(model);

        Cursor cursor = null;
        try {
            cursor = db.query(dbHelper.getTableName(model), columns.toArray(new String[columns.size()]),
                            where, selectionArgs, groupBy, having, orderBy, limit);
            return iterate(model, cursor, false);
        } catch (Exception e) {
            Log.d("DBAdapter", e.getStackTrace().toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    /**
     * Realiza uma consulta utilizando o método
     * {@link SQLiteDatabase#rawQuery(String, String[])}
     * 
     * @param model
     * @param sql
     * @param selectionArgs
     * @return
     */
    public <M> List<M> find(Class<M> model, String sql, String[] selectionArgs) {
        return find(model, sql, selectionArgs, false);
    }

    public Cursor executeQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public <M> List<M> find(Class<M> model, String sql, String[] selectionArgs, boolean lazy) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try {
            return iterate(model, cursor, lazy);
        } finally {
            cursor.close();
        }
    }

    private <M> List<M> iterate(Class<M> model, Cursor cursor, boolean lazy) {
        ModelIterator<M> models;

        if (lazy) {
            models = new ModelIterator<M>(cursor, model, null);
        } else {
            models = new ModelIterator<M>(cursor, model, this);
        }

        List<M> result = new ArrayList<M>();

        for (M m : models) {
            result.add(m);
        }

        return result;
    }

}

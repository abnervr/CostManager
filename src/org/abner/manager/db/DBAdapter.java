package org.abner.manager.db;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private ContentValues getContentValues(Model model)
                    throws IllegalAccessException {
        ContentValues initialValues = new ContentValues();

        for (Field field : dbHelper.getFields(model.getClass())) {
            field.setAccessible(true);
            Object object = field.get(model);

            if (object == null) {
                if (Model.class.isAssignableFrom(field.getType())) {
                    initialValues.putNull(field.getName() + "Id");
                } else {
                    initialValues.putNull(field.getName());
                }

            } else if (object instanceof String) {
                initialValues.put(field.getName(), (String) object);

            } else if (object instanceof Date) {
                initialValues.put(field.getName(), ((Date) object).getTime());

            } else if (object instanceof Integer) {
                initialValues.put(field.getName(), (Integer) object);

            } else if (object instanceof Long) {
                initialValues.put(field.getName(), (Long) object);

            } else if (object instanceof Boolean) {
                Boolean booleanValue = (Boolean) object;
                initialValues.put(field.getName(), booleanValue ? 1 : 0);

            } else if (object instanceof BigDecimal) {
                BigDecimal bigDecimalValue = (BigDecimal) object;
                bigDecimalValue.setScale(6, RoundingMode.HALF_UP);
                initialValues.put(field.getName(), bigDecimalValue.toString());

            } else if (object instanceof Model) {
                Model parent = (Model) object;
                initialValues.put(field.getName() + "Id", parent.getId());
            } else {
                initialValues.put(field.getName(), object.toString());
            }
        }
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
        return findFirst(model, where, null, null, null, null, null);
    }

    public <T extends Model> List<T> findFirst(Class<T> model, String where, String[] selectionArgs, String groupBy, String having,
                    String orderBy, String limit) {
        List<Field> fields = dbHelper.getFields(model);
        String[] columns = new String[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            columns[i] = field.getName();

            if (Model.class.isAssignableFrom(field.getType())) {
                columns[i] += "Id";
            }
        }

        Cursor cursor = null;
        try {
            cursor = db.query(dbHelper.getTableName(model), columns, where,
                            selectionArgs, groupBy, having, orderBy, limit);
            return iterate(model, fields, cursor, false);
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

    public <M> List<M> find(Class<M> model, String sql, String[] selectionArgs,
                    boolean lazy) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try {
            List<Field> fields = dbHelper.getFields(model, false);
            return iterate(model, fields, cursor, lazy);
        } finally {
            cursor.close();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <M> List<M> iterate(Class<M> model, List<Field> fields,
                    Cursor cursor, boolean lazy) {
        List<M> result = new ArrayList<M>();
        try {

            if (cursor.moveToFirst()) {

                do {
                    M instance = model.newInstance();

                    for (int i = 0; i < cursor.getColumnCount()
                                    && i < fields.size(); i++) {
                        if (!cursor.isNull(i)) {
                            String columnName = cursor.getColumnName(i);
                            Field field = fields.get(i);
                            if (!columnName.equals(field.getName())
                                            && !(Model.class.isAssignableFrom(field
                                                            .getType()) && columnName
                                                            .equals(field.getName() + "Id"))) {
                                field = null;
                                for (Field f : fields) {
                                    if (f.getName().equals(columnName)
                                                    || (Model.class.isAssignableFrom(f
                                                                    .getType()) && columnName
                                                                    .equals(f.getName() + "Id"))) {
                                        field = f;
                                        break;
                                    }
                                }
                                if (field == null) {
                                    // NÃ£o achei o field para o valor que foi
                                    // consultado
                                    continue;
                                }
                            }
                            field.setAccessible(true);
                            Class<?> type = field.getType();

                            if (type.equals(Long.class)
                                            || type.equals(long.class)) {
                                field.set(instance, cursor.getLong(i));

                            } else if (type.isEnum()) {
                                field.set(instance, Enum.valueOf((Class<Enum>) type, cursor.getString(i)));

                            } else if (type.equals(Integer.class)
                                            || type.equals(int.class)) {
                                field.set(instance, cursor.getInt(i));

                            } else if (type.equals(Boolean.class)
                                            || type.equals(boolean.class)) {
                                field.set(instance,
                                                cursor.getInt(i) == 1 ? Boolean.TRUE
                                                                : Boolean.FALSE);

                            } else if (type.equals(String.class)) {
                                field.set(instance, cursor.getString(i));

                            } else if (type.equals(Date.class)) {
                                field.set(instance,
                                                new Date(cursor.getLong(i)));

                            } else if (type.equals(BigDecimal.class)) {
                                field.set(instance,
                                                new BigDecimal(cursor.getString(i),
                                                                new MathContext(6,
                                                                                RoundingMode.HALF_UP)));

                            } else if (Model.class.isAssignableFrom(type)) {
                                long id = cursor.getLong(i);
                                Model parent;
                                if (lazy) {
                                    parent = (Model) type.newInstance();
                                    parent.setId(id);
                                } else {
                                    parent = findById((Class<Model>) type, id);
                                }
                                field.set(instance, parent);
                            }
                        }
                    }
                    result.add(instance);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getSimpleName(),
                            e.getMessage() != null ? e.getMessage() : "<null>");
            throw new RuntimeException(e);
        }
        return result;
    }

}

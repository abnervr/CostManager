package org.abner.manager.db.model;

import java.util.ArrayList;
import java.util.List;

import org.abner.manager.db.DBAdapter;
import org.abner.manager.db.model.reader.Column;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class ModelHelper<T> {

    private final Class<T> model;
    private final List<Column> columns = new ArrayList<Column>();

    public ModelHelper(Class<T> model) {
        this(model, null);
    }

    public ModelHelper(Class<T> model, DBAdapter db) {
        this.model = model;
        this.columns.addAll(Column.getColumns(model, db));
    }

    public T build(Cursor cursor) {
        T instance;
        try {
            instance = model.newInstance();
        } catch (Exception e) {
            Log.e("ModelBuilder", "Erro ao instanciar " + model.getName() + ": " + e.getMessage());
            return null;
        }

        for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
            if (!cursor.isNull(columnIndex)) {
                Column column = getColumn(cursor.getColumnName(columnIndex));
                if (column != null) {
                    column.readCursorAndSetValue(instance, cursor, columnIndex);
                }
            }
        }
        return instance;
    }

    private Column getColumn(String columnName) {
        for (Column column : columns) {
            if (column.getName().equals(columnName)) {
                return column;
            }
        }
        return null;
    }

    public ContentValues getContentValues(T model) {
        ContentValues contentValues = new ContentValues();

        for (Column column : columns) {
            column.putFieldValueInContent(contentValues, model);
        }
        return contentValues;
    }

    public List<String> getColumnNames() {
        List<String> names = new ArrayList<String>();

        for (Column column : columns) {
            names.add(column.getName());
        }

        return names;
    }
}

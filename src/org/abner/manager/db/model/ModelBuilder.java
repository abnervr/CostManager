package org.abner.manager.db.model;

import java.util.ArrayList;
import java.util.List;

import org.abner.manager.db.DBAdapter;
import org.abner.manager.db.model.reader.FieldReader;

import android.database.Cursor;
import android.util.Log;

public class ModelBuilder<T> {

    private final Class<T> model;
    private final List<FieldReader> fields = new ArrayList<FieldReader>();

    public ModelBuilder(Class<T> model, DBAdapter db) {
        this.model = model;
        this.fields.addAll(FieldReader.getReaders(model, db));
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
                FieldReader fieldReader = getFieldReader(cursor.getColumnName(columnIndex));
                if (fieldReader != null) {
                    fieldReader.readCursorAndSetValue(instance, cursor, columnIndex);
                }
            }
        }
        return instance;
    }

    private FieldReader getFieldReader(String columnName) {
        for (FieldReader f : fields) {
            if (f.getName().equals(columnName)) {
                return f;
            }
        }

        return null;
    }
}

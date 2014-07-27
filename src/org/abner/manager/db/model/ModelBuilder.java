package org.abner.manager.db.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.abner.manager.db.DBAdapter;
import org.abner.manager.db.model.reader.FieldReader;
import org.abner.manager.model.Model;

import android.database.Cursor;
import android.util.Log;

public class ModelBuilder<T> {

    private final Class<T> model;
    private final List<FieldReader<?>> fields = new ArrayList<FieldReader<?>>();

    public ModelBuilder(Class<T> model, List<Field> fields, DBAdapter db) {
        this.model = model;
        for (Field field : fields) {
            this.fields.add(FieldReader.getReader(field, db));
        }
    }

    public T build(Cursor cursor) {
        T instance;
        try {
            instance = model.newInstance();
        } catch (Exception e) {
            Log.e("ModelBuilder", "Erro ao instanciar " + model.getName() + ": " + e.getMessage());
            return null;
        }

        for (int i = 0; i < cursor.getColumnCount() && i < fields.size(); i++) {
            if (!cursor.isNull(i)) {
                FieldReader<?> field = getFieldReader(i, cursor.getColumnName(i));
                if (field != null) {
                    field.readAndSet(instance, cursor, i);
                }
            }
        }
        return instance;
    }

    private FieldReader<?> getFieldReader(int i, String columnName) {
        FieldReader<?> field = fields.get(i);
        if (!columnName.equals(field.getName())
                        && !(Model.class.isAssignableFrom(field.getType())
                        && columnName.equals(field.getName() + "Id"))) {
            field = null;
            for (FieldReader<?> f : fields) {
                if (f.getName().equals(columnName)
                                || (Model.class.isAssignableFrom(f.getType())
                                && columnName.equals(f.getName() + "Id"))) {
                    return f;
                }
            }
        }
        return field;
    }
}

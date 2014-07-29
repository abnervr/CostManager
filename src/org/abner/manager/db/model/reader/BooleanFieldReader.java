package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.database.Cursor;

public class BooleanFieldReader extends FieldReader {

    public BooleanFieldReader(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        if (cursor.getInt(columnIndex) == 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}

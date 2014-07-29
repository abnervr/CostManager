package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.database.Cursor;

public class IntegerFieldReader extends FieldReader {

    public IntegerFieldReader(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return cursor.getInt(columnIndex);
    }

}

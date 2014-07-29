package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.database.Cursor;

public class StringFieldReader extends FieldReader {

    public StringFieldReader(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return cursor.getString(columnIndex);
    }

}

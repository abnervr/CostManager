package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.database.Cursor;

public class LongFieldReader extends FieldReader<Long> {

    public LongFieldReader(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return cursor.getLong(columnIndex);
    }

}

package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;
import java.util.Date;

import android.database.Cursor;

public class DateFieldReader extends FieldReader<Long> {

    public DateFieldReader(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return new Date(cursor.getLong(columnIndex));
    }

}

package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class DateColumn extends Column {

    public DateColumn(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return new Date(cursor.getLong(columnIndex));
    }

    @Override
    protected void putContentValue(ContentValues contentValues, Object value) {
        contentValues.put(getName(), ((Date) value).getTime());
    }

}

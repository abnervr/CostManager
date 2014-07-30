package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class LongColumn extends Column {

    public LongColumn(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return cursor.getLong(columnIndex);
    }

    @Override
    protected void putContentValue(ContentValues contentValues, Object value) {
        contentValues.put(getName(), (Long) value);
    }
}

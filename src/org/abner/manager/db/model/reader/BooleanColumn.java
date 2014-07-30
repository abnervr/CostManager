package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class BooleanColumn extends Column {

    public BooleanColumn(Field field) {
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

    @Override
    protected void putContentValue(ContentValues contentValues, Object value) {
        Boolean booleanValue = (Boolean) value;
        contentValues.put(getName(), booleanValue ? 1 : 0);
    }

}

package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;

public class EnumColumn extends Column {

    public EnumColumn(Field field) {
        super(field);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return Enum.valueOf((Class<Enum>) getType(), cursor.getString(columnIndex));
    }

    @Override
    protected void putContentValue(ContentValues contentValues, Object value) {
        contentValues.put(getName(), value.toString());
    }

}

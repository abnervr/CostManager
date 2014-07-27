package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import android.database.Cursor;

public class EnumFieldReader extends FieldReader<Enum<?>> {

    public EnumFieldReader(Field field) {
        super(field);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return Enum.valueOf((Class<Enum>) getType(), cursor.getString(columnIndex));
    }

}

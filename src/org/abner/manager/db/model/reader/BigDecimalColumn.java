package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import android.content.ContentValues;
import android.database.Cursor;

public class BigDecimalColumn extends Column {

    public BigDecimalColumn(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return new BigDecimal(cursor.getString(columnIndex),
                        new MathContext(6, RoundingMode.HALF_UP));
    }

    @Override
    protected void putContentValue(ContentValues contentValues, Object value) {
        BigDecimal bigDecimalValue = (BigDecimal) value;
        bigDecimalValue.setScale(6, RoundingMode.HALF_UP);
        contentValues.put(getName(), bigDecimalValue.toString());
    }

}

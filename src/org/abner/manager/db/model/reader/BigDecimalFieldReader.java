package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import android.database.Cursor;

public class BigDecimalFieldReader extends FieldReader<Long> {

    public BigDecimalFieldReader(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        return new BigDecimal(cursor.getString(columnIndex),
                        new MathContext(6, RoundingMode.HALF_UP));
    }

}

package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.abner.manager.db.DBAdapter;
import org.abner.manager.db.ModelProperties;
import org.abner.manager.model.Model;

import android.database.Cursor;
import android.util.Log;

public abstract class FieldReader {

    private final Field field;

    public static List<FieldReader> getReaders(Class<?> model, DBAdapter db) {
        List<FieldReader> readers = new ArrayList<FieldReader>();

        for (Field field : ModelProperties.getFields(model)) {
            readers.add(FieldReader.getReader(field, db));
        }

        return readers;
    }

    private static FieldReader getReader(Field field, DBAdapter db) {
        Class<?> type = field.getType();

        if (type.equals(Long.class) || type.equals(long.class)) {
            return new LongFieldReader(field);
        } else if (type.isEnum()) {
            return new EnumFieldReader(field);
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return new IntegerFieldReader(field);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return new BooleanFieldReader(field);
        } else if (type.equals(String.class)) {
            return new StringFieldReader(field);
        } else if (type.equals(Date.class)) {
            return new DateFieldReader(field);
        } else if (type.equals(BigDecimal.class)) {
            return new BigDecimalFieldReader(field);
        } else if (Model.class.isAssignableFrom(type)) {
            if (db != null) {
                return new ModelFieldReader(db, field);
            } else {
                return new LazyModelFieldReader(field);
            }
        } else {
            throw new RuntimeException("Tipo de field não suportado.");
        }
    }

    protected FieldReader(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getType() {
        return field.getType();
    }

    public void readCursorAndSetValue(Object instance, Cursor cursor, int columnIndex) {
        try {
            field.set(instance, read(cursor, columnIndex));
        } catch (Exception e) {
            Log.e("FieldReader", "Erro setando field " + field.getName(), e);
        }
    }

    protected abstract Object read(Cursor cursor, int columnIndex);

}

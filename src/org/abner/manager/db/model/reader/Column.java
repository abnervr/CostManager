package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.abner.manager.db.DBAdapter;
import org.abner.manager.db.model.ModelProperties;
import org.abner.manager.model.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public abstract class Column {

    private final Field field;

    public static List<Column> getColumns(Class<?> model, DBAdapter db) {
        List<Column> readers = new ArrayList<Column>();

        for (Field field : ModelProperties.getFields(model)) {
            readers.add(Column.getColumn(field, db));
        }

        return readers;
    }

    private static Column getColumn(Field field, DBAdapter db) {
        Class<?> type = field.getType();

        if (type.equals(Long.class) || type.equals(long.class)) {
            return new LongColumn(field);
        } else if (type.isEnum()) {
            return new EnumColumn(field);
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return new IntegerColumn(field);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return new BooleanColumn(field);
        } else if (type.equals(String.class)) {
            return new StringColumn(field);
        } else if (type.equals(Date.class)) {
            return new DateColumn(field);
        } else if (type.equals(BigDecimal.class)) {
            return new BigDecimalColumn(field);
        } else if (Model.class.isAssignableFrom(type)) {
            if (db != null) {
                return new ModelColumn(db, field);
            } else {
                return new LazyModelColumn(field);
            }
        } else {
            throw new RuntimeException("Tipo de field não suportado.");
        }
    }

    protected Column(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    public String getName() {
        return field.getName();
    }

    protected Class<?> getType() {
        return field.getType();
    }

    public void putFieldValueInContent(ContentValues contentValues, Object object) {
        Object value = readFieldValue(object);

        if (value == null) {
            contentValues.putNull(getName());
        } else {
            putContentValue(contentValues, value);
        }
    }

    private Object readFieldValue(Object object) {
        try {
            return field.get(object);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Erro ao ler " + getName(), e);
            return null;
        }
    }

    protected abstract void putContentValue(ContentValues contentValues, Object value);

    public void readCursorAndSetValue(Object instance, Cursor cursor, int columnIndex) {
        try {
            field.set(instance, read(cursor, columnIndex));
        } catch (Exception e) {
            Log.e("FieldReader", "Erro setando field " + field.getName(), e);
        }
    }

    protected abstract Object read(Cursor cursor, int columnIndex);

}

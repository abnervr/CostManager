package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import org.abner.manager.model.Model;

import android.content.ContentValues;

public abstract class AbstractModelColumn extends Column {

    protected AbstractModelColumn(Field field) {
        super(field);
    }

    @Override
    public String getName() {
        return super.getName() + "Id";
    }

    @Override
    protected void putContentValue(ContentValues contentValues, Object value) {
        contentValues.put(getName(), ((Model) value).getId());
    }

}

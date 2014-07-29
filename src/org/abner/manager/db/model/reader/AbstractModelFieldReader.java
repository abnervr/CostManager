package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

public abstract class AbstractModelFieldReader extends FieldReader {

    protected AbstractModelFieldReader(Field field) {
        super(field);
    }

    @Override
    public String getName() {
        return super.getName() + "Id";
    }
}

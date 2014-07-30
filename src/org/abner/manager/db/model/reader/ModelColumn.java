package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import org.abner.manager.db.DBAdapter;
import org.abner.manager.model.Model;

import android.database.Cursor;

public class ModelColumn extends AbstractModelColumn {

    private final DBAdapter db;

    public ModelColumn(DBAdapter db, Field field) {
        super(field);
        this.db = db;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object read(Cursor cursor, int columnIndex) {
        long modelId = cursor.getLong(columnIndex);

        return db.findById((Class<Model>) getType(), modelId);
    }

}

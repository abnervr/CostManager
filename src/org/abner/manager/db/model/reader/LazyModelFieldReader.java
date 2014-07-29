package org.abner.manager.db.model.reader;

import java.lang.reflect.Field;

import org.abner.manager.model.Model;

import android.database.Cursor;
import android.util.Log;

public class LazyModelFieldReader extends AbstractModelFieldReader {

    public LazyModelFieldReader(Field field) {
        super(field);
    }

    @Override
    public Object read(Cursor cursor, int columnIndex) {
        long modelId = cursor.getLong(columnIndex);

        try {
            Model model = (Model) getType().newInstance();
            model.setId(modelId);
            return model;
        } catch (Exception e) {
            Log.e("ModelBuild", getType().getName() + " não tem um construtor sem parâmetros");
            return null;
        }
    }

}

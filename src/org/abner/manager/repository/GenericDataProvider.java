package org.abner.manager.repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.abner.manager.db.DBAdapter;

import android.content.Context;

public abstract class GenericDataProvider<M> implements DataProvider<M> {

    protected Class<M> model;
    protected Context context;
    protected DBAdapter db;

    @SuppressWarnings("unchecked")
    public GenericDataProvider(Context context) {
        this.model = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.context = context;
        this.db = new DBAdapter(context);
    }

    protected List<M> find(String sql, Object... selectionArgs) {
        try {
            db.open();

            String[] args = buildSelectionArgs(selectionArgs);
            return db.find(model, sql, args);
        } finally {
            db.close();
        }
    }

    private String[] buildSelectionArgs(Object... selectionArgs) {
        String[] args = null;
        if (selectionArgs != null) {
            args = new String[selectionArgs.length];
            for (int i = 0; i < selectionArgs.length; i++) {
                if (selectionArgs[i] != null) {
                    args[i] = selectionArgs[i].toString();
                }
            }
        }
        return args;
    }

}

package org.abner.manager.repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.abner.manager.db.DBAdapter;
import org.abner.manager.model.Model;

import android.content.Context;

public class GenericDAO<M extends Model> implements Repository<M> {

    protected Class<M> model;
    protected Context context;
    protected DBAdapter db;

    @SuppressWarnings("unchecked")
    public GenericDAO(Context context) {
        this.model = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.context = context;
        this.db = new DBAdapter(context);
    }

    @Override
    public List<M> find() {
        try {
            db.open();
            return db.find(model);
        } finally {
            db.close();
        }
    }

    @Override
    public Iterable<M> iterate() {
        return find();
    }

    protected List<M> findWhere(String where) {
        try {
            db.open();
            return db.find(model, where);
        } finally {
            db.close();
        }
    }

    protected List<M> find(String sql, Object... selectionArgs) {
        try {
            db.open();
            String[] args = null;
            if (selectionArgs != null) {
                args = new String[selectionArgs.length];
                for (int i = 0; i < selectionArgs.length; i++) {
                    if (selectionArgs[i] != null) {
                        args[i] = selectionArgs[i].toString();
                    }
                }
            }
            return db.find(model, sql, args);
        } finally {
            db.close();
        }
    }

    @Override
    public M find(Long id) {
        try {
            db.open();
            return db.findById(model, id);
        } finally {
            db.close();
        }
    }

    @Override
    public void remove(M entity) {
        try {
            db.open();
            db.remove(entity);
        } finally {
            db.close();
        }
    }

    @Override
    public long insert(M entity) {
        try {
            db.open();
            return db.insert(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.close();
        }
    }

    @Override
    public long update(M entity) {
        try {
            db.open();
            return db.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.close();
        }
    }

}

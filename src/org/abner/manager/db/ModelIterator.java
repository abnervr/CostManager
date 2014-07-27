package org.abner.manager.db;

import java.util.Iterator;

import org.abner.manager.db.model.ModelBuilder;

import android.database.Cursor;

public class ModelIterator<T> implements Iterable<T>, Iterator<T> {

    private final Cursor cursor;
    private final ModelBuilder<T> builder;

    private boolean first;

    public ModelIterator(Cursor cursor, Class<T> model, DBAdapter db) {
        this.cursor = cursor;
        this.builder = new ModelBuilder<T>(model, ModelProperties.getFields(model), db);

        this.first = true;
    }

    @Override
    public boolean hasNext() {
        if (first) {
            first = false;
            return cursor.moveToFirst();
        } else {
            return cursor.moveToNext();
        }
    }

    @Override
    public T next() {
        return builder.build(cursor);
    }

    @Override
    public void remove() {}

    @Override
    public Iterator<T> iterator() {
        return this;
    }

}

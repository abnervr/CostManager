package org.abner.manager.activities.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public abstract class AbstractMainAdapter<T> extends ArrayAdapter<T> implements MainAdapter {

    public AbstractMainAdapter(Context context) {
        super(context, android.R.id.list, new ArrayList<T>());
    }

    @Override
    public void update() {
        clear();
        addAll(getItems());
    }

    protected abstract List<T> getItems();

}

package org.abner.manager.activities.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public abstract class MainAdapter<T> extends ArrayAdapter<T> {

    public MainAdapter(Context context) {
        super(context, android.R.id.list, new ArrayList<T>());
        addAll(getItems());
    }

    public void update() {
        clear();
        addAll(getItems());
    }

    protected abstract List<T> getItems();

}

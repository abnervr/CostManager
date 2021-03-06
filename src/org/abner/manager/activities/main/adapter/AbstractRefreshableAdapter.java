package org.abner.manager.activities.main.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public abstract class AbstractRefreshableAdapter<T> extends ArrayAdapter<T> implements RefreshableAdapter {

    public AbstractRefreshableAdapter(Context context) {
        super(context, android.R.id.list, new ArrayList<T>());
    }

    @Override
    public void refresh() {
        clear();
        addAll(getItems());
    }

    protected abstract List<T> getItems();

}

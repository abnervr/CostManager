package org.abner.manager.activities.main.fragment;

import org.abner.manager.activities.main.MainFragment;
import org.abner.manager.activities.main.adapter.GastosAdapter;
import org.abner.manager.activities.main.adapter.gastos.Grouping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GastosFragment extends MainFragment {

    private Grouping grouping;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(GROUPING_ID)) {
            grouping = Grouping.valueOf(getArguments().getString(GROUPING_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View onCreateView = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new GastosAdapter(getActivity(), grouping));
        return onCreateView;
    }
}

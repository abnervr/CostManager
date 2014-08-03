package org.abner.manager.activities.main.fragment;

import org.abner.manager.R;
import org.abner.manager.activities.main.MainFragment;
import org.abner.manager.activities.main.adapter.GastosAdapter;
import org.abner.manager.activities.main.adapter.gastos.Grouping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class GastosFragment extends MainFragment {

    private Grouping grouping;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int index = getActivity().getActionBar().getSelectedNavigationIndex();
        if (index >= 0) {
            grouping = Grouping.values()[index];
        } else if (getArguments().containsKey(GROUPING_ID)) {
            grouping = Grouping.valueOf(getArguments().getString(GROUPING_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View onCreateView = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new GastosAdapter(getActivity(), grouping));
        return onCreateView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean onOptionsItemSelected = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_gasto_empresas:
                return true;
            case R.id.action_gasto_tipos:
                return true;
        }
        return onOptionsItemSelected;
    }

}

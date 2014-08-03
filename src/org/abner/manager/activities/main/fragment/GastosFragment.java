package org.abner.manager.activities.main.fragment;

import java.util.List;

import org.abner.manager.R;
import org.abner.manager.activities.main.adapter.GastosAdapter;
import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.model.views.Gasto;
import org.abner.manager.repository.views.GastosDataProvider;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class GastosFragment extends Fragment {

    public static final String GROUPING_ID = "grouping_id";

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
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);

        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(android.R.id.list);

        List<Gasto> gastos = new GastosDataProvider(getActivity()).find(grouping);
        expandableListView.setAdapter(new GastosAdapter(getActivity(), gastos));
        expandableListView.setGroupIndicator(null);

        return view;
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

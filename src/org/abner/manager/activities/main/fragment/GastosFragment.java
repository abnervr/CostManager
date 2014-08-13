package org.abner.manager.activities.main.fragment;

import java.util.List;

import org.abner.manager.R;
import org.abner.manager.activities.main.adapter.GastosAdapter;
import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.model.views.Gasto;
import org.abner.manager.repository.views.GastosDataProvider;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.SpinnerAdapter;

public class GastosFragment extends Fragment implements OnNavigationListener {

    public static final String GROUPING_ID = "grouping_id";

    private Grouping grouping;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments().containsKey(GROUPING_ID)) {
            grouping = Grouping.values()[getArguments().getInt(GROUPING_ID)];
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gasto_empresas:
                //updateFragment(Grouping.ANO);
                return true;
            case R.id.action_gasto_tipos:
                //updateFragment(Grouping.DIA);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gastos, menu);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter gastosSpinnerAdapter = new ArrayAdapter<Grouping>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        Grouping.values());
        actionBar.setListNavigationCallbacks(gastosSpinnerAdapter, this);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle("Gastos");
        actionBar.setSelectedNavigationItem(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);

        updateListViewAdapter((ExpandableListView) view.findViewById(android.R.id.list));

        return view;
    }

    private void updateListViewAdapter(ExpandableListView expandableListView) {
        if (expandableListView == null) {
            expandableListView = (ExpandableListView) getActivity().findViewById(android.R.id.list);
        }

        List<Gasto> gastos = new GastosDataProvider(getActivity()).find(grouping);
        expandableListView.setAdapter(new GastosAdapter(getActivity(), gastos));
        expandableListView.setGroupIndicator(null);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        grouping = Grouping.values()[itemPosition];
        getArguments().putInt(GROUPING_ID, itemPosition);
        updateListViewAdapter(null);
        return true;
    }

}

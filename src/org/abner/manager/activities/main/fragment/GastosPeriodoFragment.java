package org.abner.manager.activities.main.fragment;

import java.util.List;

import org.abner.manager.R;
import org.abner.manager.activities.main.adapter.GastosPeriodoAdapter;
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
import android.widget.TextView;

public class GastosPeriodoFragment extends Fragment implements OnNavigationListener {

    public static final String GROUPING_ID = "grouping_id";

    private Grouping grouping;
    private boolean empresa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments().containsKey(GROUPING_ID)) {
            grouping = (Grouping) getArguments().getSerializable(GROUPING_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gasto_empresas:
                empresa = true;
                item.setChecked(true);
                updateListViewAdapter();
                return true;
            case R.id.action_gasto_tipos:
                empresa = false;
                item.setChecked(true);
                updateListViewAdapter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gastos, menu);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SpinnerAdapter gastosSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getGroupingTitles()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                int color = getResources().getColor(android.R.color.white);
                view.setTextColor(color);
                return view;
            }
        };
        actionBar.setListNavigationCallbacks(gastosSpinnerAdapter, this);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle("Gastos");
        actionBar.setSelectedNavigationItem(getNavigationIndex());
    }

    private String[] getGroupingTitles() {
        Grouping[] values = Grouping.values();
        String[] titles = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            titles[i] = values[i].getTitle();
        }

        return titles;
    }

    private int getNavigationIndex() {
        Grouping[] values = Grouping.values();
        for (int i = 0; i < values.length; i++) {
            Grouping grouping = values[i];
            if (grouping == this.grouping) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);

        updateListViewAdapter((ExpandableListView) view.findViewById(android.R.id.list));

        return view;
    }

    private void updateListViewAdapter() {
        updateListViewAdapter((ExpandableListView) getActivity().findViewById(android.R.id.list));
    }

    private void updateListViewAdapter(ExpandableListView expandableListView) {
        List<Gasto> gastos = getGastos();
        expandableListView.setAdapter(new GastosPeriodoAdapter(getActivity(), gastos));
        expandableListView.setGroupIndicator(null);
    }

    private List<Gasto> getGastos() {
        GastosDataProvider gastosDataProvider = new GastosDataProvider(getActivity());
        if (empresa) {
            return gastosDataProvider.findByEmpresa(grouping);
        } else {
            return gastosDataProvider.findByTipo(grouping);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        grouping = Grouping.values()[itemPosition];
        updateListViewAdapter();
        return true;
    }

}

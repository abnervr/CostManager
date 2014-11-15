package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.SmsReadTask;
import org.abner.manager.SmsReadTask.OnFinishListener;
import org.abner.manager.activities.main.adapter.RefreshableAdapter;
import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.activities.main.fragment.GastosFragment;
import org.abner.manager.activities.main.fragment.GastosPeriodoFragment;
import org.abner.manager.activities.main.fragment.RelatorioFragment;
import org.abner.manager.activities.main.fragment.SmsFragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class MainFragment extends ListFragment implements OnRefreshListener, OnFinishListener {

    public static Fragment buildFragment(Program program) {
        Fragment fragment;
        switch (program) {
            case GASTOS:
                fragment = new GastosFragment();
                break;
            case RELATORIOS:
                fragment = new RelatorioFragment();
                break;
            case SMS:
                fragment = new SmsFragment();
                break;
            case GASTOS_PERIODO:
                Bundle arguments = new Bundle();
                arguments.putSerializable(GastosPeriodoFragment.GROUPING_ID, Grouping.MES);

                fragment = new GastosPeriodoFragment();
                fragment.setArguments(arguments);
                break;
            default:
                return null;
        }

        return fragment;
    }

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,
                        container, false);
        if (view instanceof SwipeRefreshLayout) {
            SwipeRefreshLayout srf = (SwipeRefreshLayout) view;
            srf.setOnRefreshListener(this);
        }
        return view;
    }

    public void restoreActionBar(String title) {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        if (getListAdapter() instanceof RefreshableAdapter) {
            ((RefreshableAdapter) getListAdapter()).refresh();
        }
    }

    @Override
    public void onRefresh() {
        SmsReadTask task = new SmsReadTask(getActivity(), this);
        task.execute();
    }

    @Override
    public void onFinish() {
        refreshList();

        SwipeRefreshLayout swipe = (SwipeRefreshLayout) getActivity().findViewById(R.id.swiperefresh);
        swipe.setRefreshing(false);
    }

}

package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.activities.main.adapter.MainAdapter;
import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.activities.main.fragment.CadastroFragment;
import org.abner.manager.activities.main.fragment.GastosFragment;
import org.abner.manager.activities.main.fragment.RelatorioFragment;
import org.abner.manager.activities.main.fragment.SmsFragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainFragment extends ListFragment {

    public static Fragment buildFragment(Program program) {
        Fragment fragment;
        switch (program) {
            case CADASTRO:
                fragment = new CadastroFragment();
                break;
            case RELATORIOS:
                fragment = new RelatorioFragment();
                break;
            case SMS:
                fragment = new SmsFragment();
                break;
            case GASTOS:
                Bundle arguments = new Bundle();
                arguments.putSerializable(GastosFragment.GROUPING_ID, Grouping.MES);

                fragment = new GastosFragment();
                fragment.setArguments(arguments);
                break;
            default:
                fragment = new MainFragment();
                break;
        }

        return fragment;
    }

    public MainFragment() {}

    @Override
    public ListView onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
        return (ListView) inflater.inflate(R.layout.fragment_main,
                        container, false);
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
        if (getListAdapter() instanceof MainAdapter) {
            ((MainAdapter) getListAdapter()).update();
        }
    }
}

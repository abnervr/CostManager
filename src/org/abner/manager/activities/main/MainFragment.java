package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.activities.main.adapter.MainAdapter;
import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.activities.main.fragment.CadastroFragment;
import org.abner.manager.activities.main.fragment.GastosFragment;
import org.abner.manager.activities.main.fragment.RelatorioFragment;
import org.abner.manager.activities.main.fragment.SmsFragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainFragment extends ListFragment {

    public static Fragment buildFragment(Program program, Grouping grouping) {
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
                arguments.putString(GastosFragment.GROUPING_ID, grouping.toString());

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

    @Override
    public void onResume() {
        super.onResume();
        if (getListAdapter() instanceof MainAdapter) {
            ((MainAdapter) getListAdapter()).update();
        }
    }
}

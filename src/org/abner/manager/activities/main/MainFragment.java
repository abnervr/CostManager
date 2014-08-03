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
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class MainFragment extends ListFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static final String GROUPING_ID = "grouping_id";

    public static MainFragment buildFragment(Program program, Grouping grouping) {
        Bundle arguments = new Bundle();
        arguments.putString(MainFragment.ARG_ITEM_ID, program.toString());

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
                arguments.putString(MainFragment.GROUPING_ID, grouping.toString());
                fragment = new GastosFragment();
                break;
            default:
                fragment = new MainFragment();
                break;
        }
        fragment.setArguments(arguments);

        return null;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,
                        container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getListAdapter() instanceof MainAdapter) {
            ((MainAdapter<?>) getListAdapter()).update();
        }
    }
}

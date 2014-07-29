package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.movimento.MovimentoActivity;
import org.abner.manager.activities.main.adapter.GastosAdapter;
import org.abner.manager.activities.main.adapter.GastosAdapter.Grouping;
import org.abner.manager.activities.main.adapter.MainAdapter;
import org.abner.manager.activities.main.adapter.MovimentoAdapter;
import org.abner.manager.activities.main.adapter.SmsAdapter;
import org.abner.manager.model.movimento.Movimento;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    private Program program;

    private Grouping grouping;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            program = Program.valueOf(getArguments().getString(ARG_ITEM_ID));
        }
        if (getArguments().containsKey(GROUPING_ID)) {
            grouping = Grouping.valueOf(getArguments().getString(GROUPING_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,
                        container, false);

        if (program != null) {
            getActivity().getActionBar().setTitle(program.getTitle());
            switch (program) {
                case CADASTRO:
                    setListAdapter(new MovimentoAdapter(getActivity()));
                    break;
                case GASTOS:
                    setListAdapter(new GastosAdapter(getActivity(), grouping));
                    break;
                case RELATORIOS:
                    break;
                case SMS:
                    setListAdapter(new SmsAdapter(getActivity()));
                    break;
                default:
                    break;
            }
        }

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (program != null) {
            switch (program) {
                case CADASTRO:
                    Movimento movimento = (Movimento) l.getItemAtPosition(position);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MovimentoActivity.ARG_MOVIMENTO, movimento);

                    Intent intent = new Intent(getActivity(), MovimentoActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case GASTOS:
                case RELATORIOS:
                case SMS:
                default:
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (program != null) {
            switch (program) {
                case CADASTRO:
                case GASTOS:
                case SMS:
                    ((MainAdapter<?>) getListAdapter()).update();
                    break;
                case RELATORIOS:
                default:
                    break;
            }
        }
    }
}

package org.abner.manager.activities.main;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.ControlActivity;
import org.abner.manager.activities.main.adapter.GastosAdapter;
import org.abner.manager.activities.main.adapter.GastosAdapter.Grouping;
import org.abner.manager.activities.main.adapter.MovimentoAdapter;
import org.abner.manager.activities.main.adapter.SmsAdapter;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.repository.movimento.MovimentoRepository;
import org.abner.manager.repository.movimento.dao.MovimentoDao;
import org.abner.manager.repository.sms.SmsRepository;
import org.abner.manager.repository.sms.dao.SmsDao;

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

    /**
     * The dummy content this fragment is presenting.
     */
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
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            program = Program.valueOf(getArguments().getString(ARG_ITEM_ID));
        }
        if (getArguments().containsKey(GROUPING_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            grouping = Grouping.valueOf(getArguments().getString(GROUPING_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,
                        container, false);

        // Show the dummy content as text in a TextView.
        if (program != null) {
            getActivity().getActionBar().setTitle(program.getTitle());
            switch (program) {
                case CADASTRO:
                    MovimentoRepository movimentoRepository = new MovimentoDao(getActivity());
                    setListAdapter(new MovimentoAdapter(getActivity(), movimentoRepository.find()));
                    break;
                case GASTOS:
                    setListAdapter(new GastosAdapter(getActivity(), grouping));
                    break;
                case RELATORIOS:
                    break;
                case SMS:
                    SmsRepository smsRepository = new SmsDao(getActivity());
                    setListAdapter(new SmsAdapter(getActivity(), smsRepository.find()));
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
                    bundle.putSerializable(ControlActivity.ARG_MOVIMENTO, movimento);

                    Intent intent = new Intent(getActivity(), ControlActivity.class);
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
                    MovimentoRepository movimentoRepository = new MovimentoDao(getActivity());
                    ((MovimentoAdapter) getListAdapter()).clear();
                    ((MovimentoAdapter) getListAdapter()).addAll(movimentoRepository.find());
                    break;
                case GASTOS:
                case RELATORIOS:
                case SMS:
                default:
                    break;
            }
        }
    }
}

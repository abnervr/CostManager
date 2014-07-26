package org.abner.manager.activities.cadastro;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.adapter.MovimentoItemAdapter;
import org.abner.manager.activities.cadastro.adapter.MovimentoItemAdapter.MovimentoItemOnClickListener;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.MovimentoItem;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovimentoItemFragment extends ListFragment implements MovimentoItemOnClickListener {

    private Movimento movimento;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movimento = (Movimento) getArguments().getSerializable("movimento");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movimento_item, container, false);

        setListAdapter(new MovimentoItemAdapter(getActivity(), movimento, this));

        return rootView;
    }

    @Override
    public void onClick(MovimentoItem item, View view) {
        switch (view.getId()) {
            case R.id.movimento_item_estabelecimento:
                DialogFragment dialog = new EmpresaFragment();

                Bundle args = new Bundle();
                args.putSerializable("movimentoItem", item);
                dialog.setArguments(args);

                dialog.show(getFragmentManager(), "Estabelecimento");
                break;
        }
    }
}

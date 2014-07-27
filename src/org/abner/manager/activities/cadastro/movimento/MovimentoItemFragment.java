package org.abner.manager.activities.cadastro.movimento;

import java.io.Serializable;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.movimento.adapter.MovimentoItemAdapter;
import org.abner.manager.activities.cadastro.movimentoitem.MovimentoItemActivity;
import org.abner.manager.model.movimento.Movimento;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MovimentoItemFragment extends ListFragment {

    private Movimento movimento;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movimento = (Movimento) getArguments().getSerializable("movimento");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        setListAdapter(new MovimentoItemAdapter(getActivity(), movimento));

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle extras = new Bundle();
        extras.putSerializable(MovimentoItemActivity.ARG_MOVIMENTO_ITEM, (Serializable) l.getItemAtPosition(position));

        Intent intent = new Intent(getActivity(), MovimentoItemActivity.class);
        intent.putExtras(extras);
        startActivity(intent);

        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MovimentoItemAdapter) getListAdapter()).updateItems();
    }

}

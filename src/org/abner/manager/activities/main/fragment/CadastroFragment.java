package org.abner.manager.activities.main.fragment;

import org.abner.manager.activities.cadastro.movimento.MovimentoActivity;
import org.abner.manager.activities.main.MainFragment;
import org.abner.manager.activities.main.adapter.MovimentoAdapter;
import org.abner.manager.model.movimento.Movimento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CadastroFragment extends MainFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View onCreateView = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new MovimentoAdapter(getActivity()));
        return onCreateView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Movimento movimento = (Movimento) l.getItemAtPosition(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(MovimentoActivity.ARG_MOVIMENTO, movimento);

        Intent intent = new Intent(getActivity(), MovimentoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

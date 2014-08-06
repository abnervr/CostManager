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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class CadastroFragment extends MainFragment {

    @Override
    public ListView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ListView lv = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new MovimentoAdapter(getActivity()));
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Movimento movimento = (Movimento) lv.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable(MovimentoActivity.ARG_MOVIMENTO_PAI, movimento);

                Intent intent = new Intent(getActivity(), MovimentoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }
        });
        return lv;
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

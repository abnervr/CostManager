package org.abner.manager.activities.main.fragment;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.empresa.EmpresaActivity;
import org.abner.manager.activities.cadastro.movimento.MovimentoActivity;
import org.abner.manager.activities.cadastro.tipo.TipoActivity;
import org.abner.manager.activities.main.MainFragment;
import org.abner.manager.activities.main.adapter.MovimentoAdapter;
import org.abner.manager.model.movimento.Movimento;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class GastosFragment extends MainFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cadastro, menu);
        restoreActionBar("Cadastro");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_adicionar:
                Intent intent = new Intent(getActivity(), MovimentoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_empresas:
                intent = new Intent(getActivity(), EmpresaActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_tipos:
                intent = new Intent(getActivity(), TipoActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setListAdapter(new MovimentoAdapter(getActivity()));

        final ListView lv = ((ListView) view.findViewById(android.R.id.list));
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

        return view;
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

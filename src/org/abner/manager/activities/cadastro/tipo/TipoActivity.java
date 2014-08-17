package org.abner.manager.activities.cadastro.tipo;

import org.abner.manager.R;
import org.abner.manager.model.empresa.Tipo;
import org.abner.manager.repository.empresa.TipoRepository;
import org.abner.manager.repository.empresa.dao.TipoDAO;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class TipoActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo);

        setListAdapter(new TipoAdapter(this, new TipoDAO(this).find()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Tipo tipo = (Tipo) l.getItemAtPosition(position);
        TipoRepository tipoRepository = new TipoDAO(this);
        Tipo tipoByDescricao = tipoRepository.findByDescricao(tipo.getDescricao());
        String text = tipo.getId() + " vs ";
        if (tipoByDescricao != null) {
            text += tipoByDescricao.getId();
        } else {
            text += " null";
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}

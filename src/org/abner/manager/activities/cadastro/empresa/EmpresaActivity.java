package org.abner.manager.activities.cadastro.empresa;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.common.EmpresaFragment;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.repository.empresa.dao.EmpresaDao;

import android.app.DialogFragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class EmpresaActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        setListAdapter(new EmpresaAdapter(this, new EmpresaDao(this).find()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Empresa empresa = (Empresa) l.getItemAtPosition(position);

        DialogFragment d = new EmpresaFragment();

        Bundle args = new Bundle();
        args.putSerializable(EmpresaFragment.ARG_EMPRESA, empresa);
        d.setArguments(args);

        d.show(getFragmentManager(), "EditEmpresa");
    }
}

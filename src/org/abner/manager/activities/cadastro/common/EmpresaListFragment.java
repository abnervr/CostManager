package org.abner.manager.activities.cadastro.common;

import java.io.Serializable;
import java.util.List;

import org.abner.manager.activities.cadastro.common.EmpresaFragment.OnEmpresaCreatedListener;
import org.abner.manager.activities.cadastro.movimento.adapter.EmpresaAdapter;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.repository.empresa.dao.EmpresaDao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@SuppressWarnings("serial")
public class EmpresaListFragment extends DialogFragment implements OnEmpresaCreatedListener, Serializable {

    private Empresa empresa;
    private List<Empresa> empresas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Movimento movimento = (Movimento) getArguments().getSerializable("movimento");
            empresas = new EmpresaDao(getActivity()).findOrderingByUse(movimento);
            empresa = movimento.getEmpresa();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (!empresas.isEmpty()) {
            int checkedItem = -1;
            if (empresa != null) {
                checkedItem = empresas.indexOf(empresa);
            }
            builder.setSingleChoiceItems(new EmpresaAdapter(getActivity(), empresas), checkedItem, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("On empresa selected", String.valueOf(which));

                    Empresa empresa = empresas.get(which);

                    onEmpresaCreated(empresa);
                }

            });
            builder.setNeutralButton("Novo Item", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showNewEmpresaFragment();
                }

            });
        } else {
            showNewEmpresaFragment();
        }
        return builder.create();
    }

    private void showNewEmpresaFragment() {
        DialogFragment d = new EmpresaFragment();

        Bundle args = new Bundle();
        args.putSerializable(EmpresaFragment.ARG_LISTENER, EmpresaListFragment.this);
        d.setArguments(args);

        d.show(getFragmentManager(), "NewEmpresa");
    }

    @Override
    public void onEmpresaCreated(Empresa empresa) {
        if (empresa != null) {
            Intent intent = new Intent();
            intent.putExtra("EMPRESA", empresa);
            getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
        }
        dismiss();
    }

}

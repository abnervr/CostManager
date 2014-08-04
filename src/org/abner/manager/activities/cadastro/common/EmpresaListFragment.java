package org.abner.manager.activities.cadastro.common;

import java.io.Serializable;
import java.util.List;

import org.abner.manager.activities.cadastro.common.EmpresaFragment.OnEmpresaCreatedListener;
import org.abner.manager.activities.cadastro.movimento.adapter.EmpresaAdapter;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.repository.empresa.dao.EmpresaDAO;
import org.abner.manager.repository.movimento.dao.MovimentoDao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

@SuppressWarnings("serial")
public class EmpresaListFragment extends DialogFragment implements OnEmpresaCreatedListener, Serializable {

    private Movimento movimento;
    private List<Empresa> empresas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movimento = (Movimento) getArguments().getSerializable("movimento");
        }
        empresas = new EmpresaDAO(getActivity()).find();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (!empresas.isEmpty()) {
            int checkedItem = -1;
            if (movimento.getEmpresa() != null) {
                checkedItem = empresas.indexOf(movimento.getEmpresa());
            }
            builder.setSingleChoiceItems(new EmpresaAdapter(getActivity(), empresas), checkedItem, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("On empresa selected", String.valueOf(which));

                    Empresa empresa = empresas.get(which);

                    onEmpresaCreated(empresa);
                    dismiss();
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
            movimento.setEmpresa(empresa);
            new MovimentoDao(getActivity()).update(movimento);

            getTargetFragment().onActivityResult(getTargetRequestCode(), 1, null);
        }
        if (empresas.isEmpty()) {
            dismiss();
        }
    }

}

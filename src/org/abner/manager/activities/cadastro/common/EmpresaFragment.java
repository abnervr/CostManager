package org.abner.manager.activities.cadastro.common;

import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.empresa.Tipo;
import org.abner.manager.repository.empresa.EmpresaRepository;
import org.abner.manager.repository.empresa.TipoRepository;
import org.abner.manager.repository.empresa.dao.EmpresaDao;
import org.abner.manager.repository.empresa.dao.TipoDAO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class EmpresaFragment extends DialogFragment {

    public static final String ARG_LISTENER = "arg_listener";

    public static final String ARG_EMPRESA = "arg_empresa";

    public interface OnEmpresaCreatedListener {

        void onEmpresaCreated(Empresa empresa);
    }

    private OnEmpresaCreatedListener listener;

    private Empresa empresa = new Empresa();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_LISTENER)) {
                listener = (OnEmpresaCreatedListener) getArguments().getSerializable(ARG_LISTENER);
            }
            if (getArguments().containsKey(ARG_EMPRESA)) {
                empresa = (Empresa) getArguments().getSerializable(ARG_EMPRESA);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.layout_empresa, null);

        EditText editTextNome = (EditText) view.findViewById(R.id.empresa_nome);
        editTextNome.setText(empresa.getNome());

        final AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.empresa_tipo);
        if (empresa.getTipo() != null) {
            textView.setText(empresa.getTipo().getDescricao());
        }

        ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getTipos());
        textView.setAdapter(adapter);

        builder.setView(view)
                        .setTitle(R.string.empresa)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ok, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) view.findViewById(R.id.empresa_nome);
                                String nome = editText.getText().toString();

                                if (!nome.trim().isEmpty()) {
                                    Tipo tipo = getTipo(textView);

                                    empresa.setNome(nome);
                                    empresa.setTipo(tipo);
                                    EmpresaRepository empresaRepository = new EmpresaDao(getActivity());
                                    if (empresa.getId() == null) {
                                        empresaRepository.insert(empresa);
                                    } else {
                                        empresaRepository.update(empresa);
                                    }
                                    dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Nome inválido", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onEmpresaCreated(empresa);
        }
    }

    private String[] getTipos() {
        List<Tipo> tipos = new TipoDAO(getActivity()).find();

        String[] objects = new String[tipos.size()];
        for (int i = 0; i < tipos.size(); i++) {
            Tipo tipo = tipos.get(i);
            objects[i] = tipo.getDescricao();
        }
        return objects;
    }

    private Tipo getTipo(final AutoCompleteTextView textView) {
        String descricao = textView.getText().toString().trim();
        if (!descricao.isEmpty()) {
            TipoRepository tipoRepository = new TipoDAO(getActivity());
            Tipo tipo = tipoRepository.findByDescricao(descricao);
            if (tipo == null) {
                tipo = new Tipo();
                tipo.setDescricao(descricao);
                tipoRepository.insert(tipo);
            }
            return tipo;
        }
        return null;
    }
}

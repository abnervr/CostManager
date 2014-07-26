package org.abner.manager.activities.cadastro.movimento;

import java.util.List;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.movimento.adapter.EmpresaAdapter;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.empresa.Tipo;
import org.abner.manager.model.movimento.MovimentoItem;
import org.abner.manager.repository.empresa.TipoRepository;
import org.abner.manager.repository.empresa.dao.EmpresaDAO;
import org.abner.manager.repository.empresa.dao.TipoDAO;
import org.abner.manager.repository.movimento.dao.MovimentoItemDao;

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

    private MovimentoItem item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (MovimentoItem) getArguments().getSerializable("movimentoItem");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final List<Empresa> empresas = new EmpresaDAO(getActivity()).find();
        if (!empresas.isEmpty()) {
            int checkedItem = -1;
            if (item.getEmpresa() != null) {
                checkedItem = empresas.indexOf(item.getEmpresa());
            }
            builder.setSingleChoiceItems(new EmpresaAdapter(getActivity(), empresas), checkedItem, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    item.setEmpresa(empresas.get(which));
                    new MovimentoItemDao(getActivity()).update(item);
                    dialog.dismiss();
                }
            });
        } else {
            final View view = getActivity().getLayoutInflater().inflate(R.layout.layout_empresa, null);

            final AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.empresa_tipo);

            List<Tipo> tipos = new TipoDAO(getActivity()).find();

            String[] objects = new String[tipos.size()];
            for (int i = 0; i < tipos.size(); i++) {
                Tipo tipo = tipos.get(i);
                objects[i] = tipo.getDescricao();
            }
            ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, objects);
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

                                        Empresa empresa = new Empresa();
                                        empresa.setNome(nome);
                                        empresa.setTipo(tipo);
                                        new EmpresaDAO(getActivity()).insert(empresa);

                                        item.setEmpresa(empresa);
                                        new MovimentoItemDao(getActivity()).update(item);
                                        dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Nome inválido", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

        }
        return builder.create();
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

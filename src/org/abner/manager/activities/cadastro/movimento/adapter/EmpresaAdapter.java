package org.abner.manager.activities.cadastro.movimento.adapter;

import java.util.List;

import org.abner.manager.model.empresa.Empresa;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EmpresaAdapter extends ArrayAdapter<Empresa> {

    public EmpresaAdapter(Context context, List<Empresa> empresas) {
        super(context, android.R.layout.select_dialog_singlechoice, empresas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ((TextView) view).setText(getItem(position).getNome());

        return view;
    }
}

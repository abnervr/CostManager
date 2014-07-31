package org.abner.manager.activities.cadastro.empresa;

import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.empresa.Empresa;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EmpresaAdapter extends ArrayAdapter<Empresa> {

    private final LayoutInflater inflater;

    public EmpresaAdapter(Activity activity, List<Empresa> empresas) {
        super(activity, android.R.id.list, empresas);
        inflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_row, parent, false);
        }

        Empresa item = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.name);
        textView.setText(item.getNome());

        textView = (TextView) convertView.findViewById(R.id.subname);
        if (item.getTipo() != null) {
            textView.setText(item.getTipo().getDescricao());
        } else {
            textView.setText(null);
        }

        return convertView;
    }
}

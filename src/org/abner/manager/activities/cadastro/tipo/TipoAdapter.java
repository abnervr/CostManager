package org.abner.manager.activities.cadastro.tipo;

import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.empresa.Tipo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TipoAdapter extends ArrayAdapter<Tipo> {

    private final LayoutInflater inflater;

    public TipoAdapter(Activity activity, List<Tipo> tipos) {
        super(activity, android.R.id.list, tipos);
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_row, parent, false);
        }

        Tipo tipo = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.name);
        textView.setText(tipo.getDescricao());

        return convertView;
    }
}

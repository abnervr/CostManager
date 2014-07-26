package org.abner.manager.activities.main.adapter;

import java.text.NumberFormat;
import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.TipoMovimento;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MovimentoAdapter extends ArrayAdapter<Movimento> {

    private final Activity context;

    public MovimentoAdapter(Activity context, List<Movimento> movimentos) {
        super(context, android.R.id.list, movimentos);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_row, parent, false);
        }

        Movimento movimento = getItem(position);

        TextView tv = (TextView) convertView.findViewById(R.id.name);
        TextView subname = (TextView) convertView.findViewById(R.id.subname);
        String name = DateFormat.getDateFormat(context).format(movimento.getData());
        name += " ";
        name += DateFormat.getTimeFormat(context).format(movimento.getData());

        tv.setText(name);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (movimento.getValor() != null) {
            if (movimento.getTipo() == TipoMovimento.CREDITO) {
                subname.setText(format.format(movimento.getValor()));
            } else {
                subname.setText(format.format(movimento.getValor().negate()));
            }
        }
        return convertView;
    }
}

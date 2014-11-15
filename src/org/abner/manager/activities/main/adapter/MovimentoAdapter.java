package org.abner.manager.activities.main.adapter;

import java.text.NumberFormat;
import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.repository.movimento.dao.MovimentoDao;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MovimentoAdapter extends AbstractRefreshableAdapter<Movimento> {

    private final LayoutInflater inflater;

    public MovimentoAdapter(Activity context) {
        super(context);
        inflater = context.getLayoutInflater();
    }

    @Override
    protected List<Movimento> getItems() {
        return new MovimentoDao(getContext()).find();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_movimento_row, parent, false);
        }

        Movimento movimento = getItem(position);

        TextView view = (TextView) convertView.findViewById(R.id.movimento_data);
        view.setText(getDateFormatted(movimento));

        view = (TextView) convertView.findViewById(R.id.movimento_empresa);
        if (movimento.getEmpresa() != null) {
            view.setText(movimento.getEmpresa().getNome());
        } else {
            view.setText(null);
        }

        view = (TextView) convertView.findViewById(R.id.movimento_valor);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (movimento.getValor() != null) {
            if (movimento.getTipo() == TipoMovimento.CREDITO) {
                view.setText(format.format(movimento.getValor()));
            } else {
                view.setText(format.format(movimento.getValor().negate()));
            }
        } else {
            view.setText(null);
        }
        return convertView;
    }

    private String getDateFormatted(Movimento movimento) {
        String name = DateFormat.format("E", movimento.getData()).toString();
        name += ", ";
        name += DateFormat.getDateFormat(getContext()).format(movimento.getData());
        name += " ";
        name += DateFormat.getTimeFormat(getContext()).format(movimento.getData());
        return name;
    }
}

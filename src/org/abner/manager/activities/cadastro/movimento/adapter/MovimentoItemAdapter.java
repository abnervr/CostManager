package org.abner.manager.activities.cadastro.movimento.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.abner.manager.R;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.MovimentoItem;
import org.abner.manager.repository.movimento.dao.MovimentoItemDao;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MovimentoItemAdapter extends ArrayAdapter<MovimentoItem> {

    private final LayoutInflater inflater;
    private final Movimento movimento;

    public MovimentoItemAdapter(Activity activity, Movimento movimento) {
        super(activity, android.R.id.list, new ArrayList<MovimentoItem>());
        this.inflater = activity.getLayoutInflater();
        this.movimento = movimento;

        updateItems();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_movimento_item_row, parent, false);
        }

        final MovimentoItem item = getItem(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        TextView view = (TextView) convertView.findViewById(R.id.movimento_item_valor);
        view.setText(format.format(item.getValor()));

        view = (TextView) convertView.findViewById(R.id.movimento_item_date_time);
        view.setText(DateFormat.getDateFormat(getContext()).format(item.getData()));
        view.setText(view.getText() + " ");
        view.setText(view.getText() + DateFormat.getTimeFormat(getContext()).format(item.getData()));

        view = (TextView) convertView.findViewById(R.id.movimento_item_empresa);
        if (item.getEmpresa() != null) {
            view.setText(item.getEmpresa().getNome());
        }

        view = (TextView) convertView.findViewById(R.id.movimento_item_produto);
        if (item.getProduto() != null) {
            view.setText(item.getProduto().getDescricao());
        }

        return convertView;
    }

    public void updateItems() {
        clear();
        addAll(new MovimentoItemDao(getContext()).findByMovimento(movimento));
    }
}

package org.abner.manager.activities.cadastro.adapter;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MovimentoItemAdapter extends ArrayAdapter<MovimentoItem> {

    public static interface MovimentoItemOnClickListener {

        void onClick(MovimentoItem item, View view);
    }

    private final LayoutInflater inflater;
    private final MovimentoItemOnClickListener onClickListener;

    public MovimentoItemAdapter(Activity activity, Movimento movimento, MovimentoItemOnClickListener onClickListener) {
        super(activity, android.R.id.list, new ArrayList<MovimentoItem>());
        this.inflater = activity.getLayoutInflater();
        this.onClickListener = onClickListener;

        addAll(new MovimentoItemDao(activity).findByMovimento(movimento));
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

        view = (TextView) convertView.findViewById(R.id.movimento_item_data);
        view.setText(DateFormat.getDateFormat(getContext()).format(item.getData()));

        view = (TextView) convertView.findViewById(R.id.movimento_item_time);
        view.setText(DateFormat.getTimeFormat(getContext()).format(item.getData()));

        view = (TextView) convertView.findViewById(R.id.movimento_item_estabelecimento);
        if (item.getEstabelecimento() != null) {
            view.setText(item.getEstabelecimento().getEmpresa().getNome());
        }
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickListener.onClick(item, v);
            }
        });

        view = (TextView) convertView.findViewById(R.id.movimento_item_produto);
        if (item.getProduto() != null) {
            view.setText(item.getProduto().getDescricao());
        }
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickListener.onClick(item, v);
            }
        });

        return convertView;
    }
}

package org.abner.manager.activities.cadastro.movimentoitem;

import java.text.NumberFormat;

import org.abner.manager.R;
import org.abner.manager.activities.cadastro.movimento.EmpresaFragment;
import org.abner.manager.model.movimento.MovimentoItem;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MovimentoItemFragment extends Fragment {

    private MovimentoItem movimentoItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movimentoItem = (MovimentoItem) getArguments().getSerializable("movimentoItem");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_movimento_item, container, false);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        TextView view = (TextView) convertView.findViewById(R.id.movimento_item_valor);
        view.setText(format.format(movimentoItem.getValor()));

        view = (TextView) convertView.findViewById(R.id.movimento_item_data);
        view.setText(DateFormat.getDateFormat(getActivity()).format(movimentoItem.getData()));

        view = (TextView) convertView.findViewById(R.id.movimento_item_time);
        view.setText(DateFormat.getTimeFormat(getActivity()).format(movimentoItem.getData()));

        view = (TextView) convertView.findViewById(R.id.movimento_item_empresa);
        if (movimentoItem.getEmpresa() != null) {
            view.setText(movimentoItem.getEmpresa().getNome());
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialog = new EmpresaFragment();

                Bundle args = new Bundle();
                args.putSerializable("movimentoItem", movimentoItem);
                dialog.setArguments(args);

                dialog.show(getFragmentManager(), "Estabelecimento");
            }
        });

        view = (TextView) convertView.findViewById(R.id.movimento_item_produto);
        if (movimentoItem.getProduto() != null) {
            view.setText(movimentoItem.getProduto().getDescricao());
        }
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        return convertView;
    }

}

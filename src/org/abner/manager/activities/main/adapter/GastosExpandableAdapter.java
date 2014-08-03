package org.abner.manager.activities.main.adapter;

import java.text.NumberFormat;
import java.util.List;

import org.abner.manager.R;
import org.abner.manager.model.views.Gasto;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class GastosExpandableAdapter extends BaseExpandableListAdapter {

    private final LayoutInflater inflater;
    private final List<Gasto> gastos;

    public GastosExpandableAdapter(Activity context, List<Gasto> gastos) {
        this.inflater = context.getLayoutInflater();
        this.gastos = gastos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_gastos_row, parent, false);
        }

        Gasto gasto = gastos.get(position);

        TextView tv = (TextView) convertView.findViewById(R.id.periodo);
        tv.setText(gasto.getPeriodo());

        NumberFormat format = NumberFormat.getCurrencyInstance();

        tv = (TextView) convertView.findViewById(R.id.valor_credito);
        tv.setText(format.format(gasto.getCredito()));

        tv = (TextView) convertView.findViewById(R.id.valor_debito);
        tv.setText(format.format(gasto.getDebito()));

        tv = (TextView) convertView.findViewById(R.id.saldo);
        tv.setText(format.format(gasto.getSaldo()));

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return gastos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return gastos.get(groupPosition).getGastos().size();
    }

    @Override
    public Gasto getGroup(int groupPosition) {
        return gastos.get(groupPosition);
    }

    @Override
    public Gasto getChild(int groupPosition, int childPosition) {
        return gastos.get(groupPosition).getGastos().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1024 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_gastos_row, parent, false);
        }

        Gasto gasto = getGroup(groupPosition);

        TextView tv = (TextView) convertView.findViewById(R.id.periodo);
        tv.setText(gasto.getPeriodo());

        NumberFormat format = NumberFormat.getCurrencyInstance();

        tv = (TextView) convertView.findViewById(R.id.valor_credito);
        tv.setText(format.format(gasto.getCredito()));

        tv = (TextView) convertView.findViewById(R.id.valor_debito);
        tv.setText(format.format(gasto.getDebito()));

        tv = (TextView) convertView.findViewById(R.id.saldo);
        tv.setText(format.format(gasto.getSaldo()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_row, parent, false);
        }

        Gasto gasto = getChild(groupPosition, childPosition);

        TextView view = (TextView) convertView.findViewById(R.id.name);
        view.setText(gasto.getEmpresa());
        if (gasto.getTipo() != null) {
            view.setText(view.getText() + " " + gasto.getTipo());
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();

        view = (TextView) convertView.findViewById(R.id.subname);
        view.setText(format.format(gasto.getDebito()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

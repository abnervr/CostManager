package org.abner.manager.activities.main.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.abner.manager.R;
import org.abner.manager.activities.main.adapter.gastos.Gasto;
import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.db.DBAdapter;
import org.abner.manager.model.movimento.TipoMovimento;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GastosAdapter extends AbstractMainAdapter<Gasto> {

    private final LayoutInflater inflater;
    private final Grouping groupBy;

    public GastosAdapter(Activity context, Grouping groupBy) {
        super(context);
        this.inflater = context.getLayoutInflater();
        this.groupBy = groupBy;
    }

    @Override
    protected List<Gasto> getItems() {
        List<Gasto> gastos = findGastos();

        for (Gasto gasto : gastos) {
            switch (groupBy) {
                case DIA:
                    String[] parts = gasto.getPeriodo().split("\\s");
                    if (parts.length == 3) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                        cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));

                        gasto.setPeriodo(
                                        DateFormat.format("E", cal.getTime())
                                                        + ", " +
                                                        DateFormat.getDateFormat(getContext()).format(cal.getTime()));
                    }
                    break;
                case SEMANA:
                    break;
                case MES:
                    Calendar cal = Calendar.getInstance();
                    parts = gasto.getPeriodo().split("\\s");
                    if (parts.length == 2) {
                        cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                        cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);

                        String formatMonth = DateFormat.format("MMMM - yyyy", cal).toString();
                        formatMonth = formatMonth.substring(0, 1).toUpperCase(Locale.US) + formatMonth.substring(1);
                        gasto.setPeriodo(formatMonth);
                    }
                    break;
                case ANO:
                    break;
            }
        }
        return gastos;
    }

    private List<Gasto> group(List<Gasto> gastos) {
        List<Gasto> groupedGastos = new ArrayList<Gasto>();

        Gasto groupGasto = null;
        for (Gasto gasto : gastos) {
            if (groupGasto != null && groupGasto.getPeriodo().equals(gasto.getPeriodo())) {
                groupGasto.addGasto(gasto);
            } else {
                groupGasto = new Gasto();
                groupGasto.setPeriodo(gasto.getPeriodo());

                groupGasto.addGasto(gasto);

                groupedGastos.add(groupGasto);
            }
        }

        return groupedGastos;
    }

    private List<Gasto> findGastos() {
        DBAdapter db = new DBAdapter(getContext());
        try {
            db.open();

            String query = "select " +
                            " strftime('" + groupBy.getFormat() + "', datetime(data/1000, 'unixepoch')) as periodo, " +
                            " empresa.nome as empresa, " +
                            " tipo.descricao as tipo, " +
                            " sum(case when tipo = '" + TipoMovimento.CREDITO.toString() + "' then valor else 0 end) as credito, " +
                            " sum(case when tipo = '" + TipoMovimento.DEBITO.toString() + "' then valor else 0 end) as debito " +
                            "from Movimento " +
                            "left outer join Empresa on (Movimento.empresaId = Empresa.id) " +
                            "left outer join Tipo on (Empresa.tipoId = Tipo.id) " +
                            "group by" +
                            " strftime('" + groupBy.getFormat() + "', datetime(data/1000, 'unixepoch')), " +
                            " empresa.nome, " +
                            " tipo.descricao " +
                            "order by 1 desc";
            List<Gasto> gastos = db.find(Gasto.class, query, null);
            return group(gastos);
        } finally {
            db.close();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_gastos_row, parent, false);
        }

        Gasto gasto = getItem(position);

        TextView tv = (TextView) convertView.findViewById(R.id.periodo);
        tv.setText(gasto.getPeriodo());

        NumberFormat format = NumberFormat.getCurrencyInstance();

        tv = (TextView) convertView.findViewById(R.id.valor_credito);
        tv.setText(format.format(gasto.getCredito()));

        tv = (TextView) convertView.findViewById(R.id.valor_debito);
        tv.setText(format.format(gasto.getDebito()));

        tv = (TextView) convertView.findViewById(R.id.saldo);
        tv.setText(format.format(gasto.getSaldo()));

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.gastos_summary);
        for (Gasto g : gasto.getGastos()) {
            View row = inflater.inflate(R.layout.layout_row, null);

            TextView view = (TextView) row.findViewById(R.id.name);
            view.setText(g.getEmpresa());
            if (g.getTipo() != null) {
                view.setText(view.getText() + " " + g.getTipo());
            }

            view = (TextView) row.findViewById(R.id.subname);
            view.setText(format.format(g.getDebito()));

            linearLayout.addView(row);
        }

        return convertView;
    }
}

package org.abner.manager.activities.main.adapter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.abner.manager.R;
import org.abner.manager.activities.main.adapter.GastosAdapter.Gasto;
import org.abner.manager.db.DBAdapter;
import org.abner.manager.model.movimento.TipoMovimento;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GastosAdapter extends MainAdapter<Gasto> {

    public static class Gasto {

        private String periodo;

        private BigDecimal credito = BigDecimal.ZERO;
        private BigDecimal debito = BigDecimal.ZERO;

        public String getPeriodo() {
            return periodo;
        }

        public void setPeriodo(String periodo) {
            this.periodo = periodo;
        }

        public BigDecimal getCredito() {
            return credito;
        }

        public void setCredito(BigDecimal credito) {
            this.credito = credito;
        }

        public BigDecimal getDebito() {
            return debito;
        }

        public void setDebito(BigDecimal debito) {
            this.debito = debito;
        }

        private BigDecimal getSaldo() {
            if (credito != null && debito != null) {
                return credito.subtract(debito);
            }
            return null;
        }

    }

    public enum Grouping {
        DIA("%Y %m %d"),
        SEMANA("%Y %W "),
        MES("%Y %m"),
        ANO("%Y");

        private String format;

        private Grouping(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

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

                        gasto.setPeriodo(DateFormat.getDateFormat(getContext()).format(cal.getTime()));
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

    private List<Gasto> findGastos() {
        DBAdapter db = new DBAdapter(getContext());
        try {
            db.open();

            String query = "select " +
                            " strftime('" + groupBy.getFormat() + "', datetime(data/1000, 'unixepoch')) as periodo, " +
                            " sum(case when tipo = '" + TipoMovimento.CREDITO.toString() + "' then valor else 0 end) as credito, " +
                            " sum(case when tipo = '" + TipoMovimento.DEBITO.toString() + "' then valor else 0 end) as debito " +
                            "from Movimento " +
                            "group by strftime('" + groupBy.getFormat() + "', datetime(data/1000, 'unixepoch')) " +
                            "order by 1 desc";
            return db.find(Gasto.class, query, null);
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

        return convertView;
    }
}

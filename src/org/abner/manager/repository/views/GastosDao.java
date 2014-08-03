package org.abner.manager.repository.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.model.views.Gasto;
import org.abner.manager.repository.GenericDataProvider;

import android.content.Context;
import android.text.format.DateFormat;

public class GastosDao extends GenericDataProvider<Gasto> {

    public GastosDao(Context context) {
        super(context);
    }

    @Override
    public List<Gasto> find() {
        return find(Grouping.MES);
    }

    public List<Gasto> find(Grouping groupBy) {
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

        List<Gasto> gastos = super.find(query);

        group(gastos);

        for (Gasto gasto : gastos) {
            gasto.setPeriodo(formatPeriodo(gasto.getPeriodo(), groupBy));
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

    private String formatPeriodo(String periodo, Grouping groupBy) {
        switch (groupBy) {
            case DIA:
                String[] parts = periodo.split("\\s");
                if (parts.length == 3) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                    cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                    cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));

                    periodo = DateFormat.format("E", cal.getTime())
                                    + ", " +
                                    DateFormat.getDateFormat(context).format(cal.getTime());
                }
                break;
            case MES:
                Calendar cal = Calendar.getInstance();
                parts = periodo.split("\\s");
                if (parts.length == 2) {
                    cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
                    cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);

                    String formatMonth = DateFormat.format("MMMM - yyyy", cal).toString();
                    formatMonth = formatMonth.substring(0, 1).toUpperCase(Locale.US) + formatMonth.substring(1);

                    periodo = formatMonth;
                }
                break;
            case ANO:
            case SEMANA:
            default:
                break;
        }
        return periodo;
    }
}

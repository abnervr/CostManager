package org.abner.manager.repository.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.abner.manager.activities.main.adapter.gastos.Grouping;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.model.views.Gasto;
import org.abner.manager.repository.GenericDataProvider;

import android.content.Context;

public class GastosDataProvider extends GenericDataProvider<Gasto> {

    public GastosDataProvider(Context context) {
        super(context);
    }

    @Override
    public List<Gasto> find() {
        return findByEmpresa(Grouping.MES);
    }

    public List<Gasto> findByEmpresa(Grouping groupBy) {
        return find(groupBy, true);
    }

    public List<Gasto> findByTipo(Grouping groupBy) {
        return find(groupBy, false);
    }

    private List<Gasto> find(Grouping groupBy, boolean empresa) {
        String query = getQuery(groupBy, empresa);

        List<Gasto> gastos = super.find(query);

        gastos = group(gastos);

        for (Gasto gasto : gastos) {
            String periodo = groupBy.format(gasto.getPeriodo());
            gasto.setPeriodo(periodo);
            Collections.sort(gasto.getGastos(), new Comparator<Gasto>() {

                @Override
                public int compare(Gasto lhs, Gasto rhs) {
                    return rhs.getDebito().compareTo(lhs.getDebito());
                }
            });
        }

        return gastos;
    }

    private String getQuery(Grouping groupBy, boolean empresa) {
        return "select " +
                        " strftime('" + groupBy.getFormat() + "', datetime(data/1000, 'unixepoch')) as periodo, " +
                        (empresa ? " empresa.nome as empresa, " : "") +
                        " tipo.descricao as tipo, " +
                        " sum(case when tipo = '" + TipoMovimento.CREDITO.toString() + "' then valor else 0 end) as credito, " +
                        " sum(case when tipo = '" + TipoMovimento.DEBITO.toString() + "' then valor else 0 end) as debito " +
                        "from Movimento " +
                        "left outer join Empresa on (Movimento.empresaId = Empresa.id) " +
                        "left outer join Tipo on (Empresa.tipoId = Tipo.id) " +
                        "group by" +
                        " strftime('" + groupBy.getFormat() + "', datetime(data/1000, 'unixepoch')), " +
                        (empresa ? " empresa.nome, " : "") +
                        " tipo.descricao " +
                        "order by 1 desc";
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

}

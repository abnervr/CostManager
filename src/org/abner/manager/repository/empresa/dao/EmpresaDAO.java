package org.abner.manager.repository.empresa.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.empresa.EmpresaRepository;

import android.content.Context;

public class EmpresaDAO extends GenericDAO<Empresa> implements EmpresaRepository {

    private static final int SAME_HOUR_SCORE = 3;
    private static final int NEXT_HOUR_SCORE = 2;

    private static final int SAME_TYPE_SCORE = 1;

    public EmpresaDAO(Context context) {
        super(context);
    }

    public List<Empresa> findOrderingByUse(Date date, TipoMovimento tipo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return findOrderingByUse(calendar, tipo);
    }

    public List<Empresa> findOrderingByUse(Calendar calendar, TipoMovimento tipo) {
        String hourCase = getSameHour(calendar);
        hourCase += " + ";
        hourCase += getNextHour(calendar, 1);
        hourCase += " + ";
        hourCase += getNextHour(calendar, -1);

        String typeCase = getSameType(tipo);

        String sql = "select e.id, e.nome, e.tipoId, sum(" + hourCase + "), sum(" + typeCase + ") from Empresa e "
                        + "left outer join Movimento m on (m.empresaId = e.id) "
                        + "group by e.id, e.nome, e.tipoId "
                        + "order by 4 desc, 5 desc, nome";
        return super.find(sql);
    }

    private String getSameType(TipoMovimento tipo) {
        String condition = "m.tipo = '" + tipo + "'";
        return caseWhen(condition, SAME_TYPE_SCORE);
    }

    private String getSameHour(Calendar calendar) {
        String formatData = "strftime('%H', datetime(m.data/1000, 'unixepoch'))";

        String condition = formatData + "  = '";
        condition += String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
        condition += "'";
        return caseWhen(condition, SAME_HOUR_SCORE);
    }

    private String getNextHour(Calendar calendar, int diff) {
        String formatData = "strftime('%H', datetime(m.data/1000, 'unixepoch'))";

        String condition = formatData + "  = '";
        condition += String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY) + diff);
        condition += "'";
        return caseWhen(condition, NEXT_HOUR_SCORE);
    }

    private String caseWhen(String condition, int score) {
        return "case when " + condition + " then " + score + " else 0 end";
    }

}

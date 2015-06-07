package org.abner.manager.repository.empresa.dao;

import java.util.Calendar;
import java.util.List;

import org.abner.manager.Settings;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.empresa.EmpresaRepository;

import android.content.Context;

public class EmpresaDao extends GenericDAO<Empresa> implements EmpresaRepository {

    private static final int SAME_HOUR_SCORE = 3;
    private static final int NEXT_HOUR_SCORE = 2;

    private static final int SAME_TYPE_SCORE = 1;

    public EmpresaDao(Context context) {
        super(context);
    }

    public List<Empresa> findOrderingByUse(Movimento movimento) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(movimento.getData());
        return findOrderingByUse(calendar, movimento.getTipo());
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

    @Override
    public Empresa findByIdentificador(String body) {
        String identificador = getIdentificador(body);
        if (identificador != null) {
            String where = "identificador = " + identificador;
            List<Empresa> empresas = findWhere(where);
            if (empresas.size() == 1) {
                return empresas.get(0);
            } else if (empresas.size() == 0) {
                Empresa empresa = new Empresa();
                empresa.setNome(identificador);
                empresa.setIdentificador(identificador);
                insert(empresa);
            }
        }
        return null;
    }

    private String getIdentificador(String body) {
        String storeStart = Settings.getStoreStart();
        if (!storeStart.trim().isEmpty()) {
            int start = body.indexOf(storeStart);
            if (start != -1 && start + storeStart.length() < body.length()) {
                String identificador = body.substring(start + storeStart.length());
                String storeEnd = Settings.getStoreEnd();
                if (!storeEnd.trim().isEmpty() && identificador.indexOf(storeEnd) != -1) {
                    identificador = identificador.substring(0, identificador.indexOf(storeEnd));
                }
                return identificador;
            }
        }
        return null;
    }
}

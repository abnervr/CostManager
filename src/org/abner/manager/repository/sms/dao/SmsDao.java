package org.abner.manager.repository.sms.dao;

import java.util.List;

import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.TipoMovimento;
import org.abner.manager.model.sms.Sms;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.empresa.EmpresaRepository;
import org.abner.manager.repository.empresa.dao.EmpresaDao;
import org.abner.manager.repository.movimento.MovimentoRepository;
import org.abner.manager.repository.movimento.dao.MovimentoDao;
import org.abner.manager.repository.sms.SmsRepository;

import android.content.Context;

public class SmsDao extends GenericDAO<Sms> implements SmsRepository {

    private final MovimentoRepository movimentoRepository;
    private final EmpresaRepository empresaRepository;

    public SmsDao(Context context) {
        super(context);
        this.movimentoRepository = new MovimentoDao(context);
        this.empresaRepository = new EmpresaDao(context);
    }

    @Override
    public long insert(Sms sms) {
        if (sms.getDebito() != null && sms.findValue() != null) {
            Movimento movimento = new Movimento();
            movimento.setData(sms.getDateSent());
            if (sms.getDebito()) {
                movimento.setTipo(TipoMovimento.DEBITO);
            } else {
                movimento.setTipo(TipoMovimento.CREDITO);
            }
            movimento.setValor(sms.findValue());
            movimento.setEmpresa(empresaRepository.findByIdentificador(sms.getBody()));
            if (movimentoRepository.insert(movimento) > 0) {
                sms.setMovimento(movimento);
            }
        }
        return super.insert(sms);
    }

    @Override
    public List<Sms> find() {
        return find("select * from sms order by dateSent desc");
    }

    @Override
    public Sms findLastSms(String... senders) {
        String sql = "select * from sms ";
        if (senders != null && senders.length > 0) {
            sql += "where address in (";
            for (int i = 0; i < senders.length; i++) {
                if (i > 0) {
                    sql += ",";
                }
                sql += "?";
            }
            sql += ")";
            sql += " order by dateSent desc limit 1";
            List<Sms> smsList = find(sql, (Object[]) senders);
            if (!smsList.isEmpty()) {
                return smsList.get(0);
            }
        }
        return null;
    }

    @Override
    public Sms findByMovimento(Movimento movimento) {
        List<Sms> smsList = findWhere("movimentoId = " + movimento.getId());
        if (smsList.size() == 1) {
            return smsList.get(0);
        }
        return null;
    }
}

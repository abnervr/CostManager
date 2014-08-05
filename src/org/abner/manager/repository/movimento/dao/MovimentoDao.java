package org.abner.manager.repository.movimento.dao;

import java.math.BigDecimal;
import java.util.List;

import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.movimento.MovimentoRepository;

import android.content.Context;

public class MovimentoDao extends GenericDAO<Movimento> implements MovimentoRepository {

    public MovimentoDao(Context context) {
        super(context);
    }

    @Override
    public List<Movimento> find() {
        return find("select * from movimento order by data desc");
    }

    @Override
    public long insert(Movimento movimento) {
        Movimento movimentoPai = movimento.getMovimentoPai();
        if (movimentoPai != null) {
            movimento.setTipo(movimentoPai.getTipo());
            if (movimento.getValor().compareTo(movimentoPai.getValor()) > 0) {
                movimento.setValor(movimentoPai.getValor());
            }
        }
        long result = super.insert(movimento);
        if (movimentoPai != null) {
            movimentoPai.setValor(movimentoPai.getValor().subtract(movimento.getValor()));
            update(movimentoPai);
        }
        return result;
    }

    @Override
    public long update(Movimento movimento) {
        Movimento movimentoPai = movimento.getMovimentoPai();
        if (movimentoPai != null) {
            movimento.setTipo(movimentoPai.getTipo());

            Movimento oldMovimento = find(movimento.getId());
            BigDecimal diff = movimento.getValor().subtract(oldMovimento.getValor());
            movimentoPai.setValor(movimentoPai.getValor().subtract(diff));
            if (movimentoPai.getValor().compareTo(BigDecimal.ZERO) < 0) {
                movimento.setValor(movimento.getValor().add(movimentoPai.getValor()));
                movimentoPai.setValor(BigDecimal.ZERO);
            }
        }
        long result = super.update(movimento);
        if (movimentoPai != null) {
            update(movimentoPai);
        }
        return result;
    }

    @Override
    public void remove(Movimento movimento) {
        Movimento movimentoPai = movimento.getMovimentoPai();
        if (movimentoPai != null) {
            movimento = find(movimento.getId());
        }
        super.remove(movimento);
        if (movimentoPai != null) {
            movimentoPai.setValor(movimentoPai.getValor().add(movimento.getValor()));
            update(movimentoPai);
        }
    }

    @Override
    public List<Movimento> getMovimentosFilhos(Movimento movimento) {
        return find("select * from Movimento where movimentoPaiId = ?", movimento.getId());
    }
}

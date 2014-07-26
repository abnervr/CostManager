package org.abner.manager.repository.movimento.dao;

import java.util.List;

import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.MovimentoItem;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.movimento.MovimentoItemRepository;

import android.content.Context;

public class MovimentoItemDao extends GenericDAO<MovimentoItem> implements MovimentoItemRepository {

    public MovimentoItemDao(Context context) {
        super(context);
    }

    @Override
    public List<MovimentoItem> findByMovimento(Movimento movimento) {
        return findWhere("movimentoId = " + movimento.getId());
    }
}

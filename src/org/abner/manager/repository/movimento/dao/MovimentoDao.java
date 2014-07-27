package org.abner.manager.repository.movimento.dao;

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

}

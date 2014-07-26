package org.abner.manager.repository.movimento.dao;

import java.util.List;

import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.MovimentoItem;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.movimento.MovimentoItemRepository;
import org.abner.manager.repository.movimento.MovimentoRepository;

import android.content.Context;

public class MovimentoDao extends GenericDAO<Movimento> implements MovimentoRepository {

    private final MovimentoItemRepository movimentoItemRepository;

    public MovimentoDao(Context context) {
        super(context);
        this.movimentoItemRepository = new MovimentoItemDao(context);
    }

    @Override
    public long insert(Movimento movimento) {
        if (super.insert(movimento) > 0) {
            MovimentoItem item = new MovimentoItem();
            item.setData(movimento.getData());
            item.setValor(movimento.getValor());
            item.setMovimento(movimento);
            movimentoItemRepository.insert(item);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<Movimento> find() {
        return find("select * from movimento order by data desc");
    }

}

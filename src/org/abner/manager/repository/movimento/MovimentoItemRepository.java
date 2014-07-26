package org.abner.manager.repository.movimento;

import java.util.List;

import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.model.movimento.MovimentoItem;
import org.abner.manager.repository.Repository;

public interface MovimentoItemRepository extends Repository<MovimentoItem> {

    List<MovimentoItem> findByMovimento(Movimento movimento);

}

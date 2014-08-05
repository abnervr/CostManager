package org.abner.manager.repository.movimento;

import java.util.List;

import org.abner.manager.model.movimento.Movimento;
import org.abner.manager.repository.Repository;

public interface MovimentoRepository extends Repository<Movimento> {

    List<Movimento> getMovimentosFilhos(Movimento movimento);
}

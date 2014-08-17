package org.abner.manager.repository.empresa;

import org.abner.manager.model.empresa.Tipo;
import org.abner.manager.repository.Repository;

public interface TipoRepository extends Repository<Tipo> {

    Tipo findByDescricao(String descricao);
}

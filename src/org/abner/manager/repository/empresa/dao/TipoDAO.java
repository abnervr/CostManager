package org.abner.manager.repository.empresa.dao;

import java.util.List;
import java.util.Locale;

import org.abner.manager.model.empresa.Tipo;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.empresa.TipoRepository;

import android.content.Context;

public class TipoDAO extends GenericDAO<Tipo> implements TipoRepository {

    public TipoDAO(Context context) {
        super(context);
    }

    @Override
    public Tipo findByDescricao(String descricao) {
        List<Tipo> items = findWhere("upper(descricao) = '" + descricao.toUpperCase(Locale.US) + "'");
        if (items.isEmpty()) {
            items = findWhere("descricao = '" + descricao + "'");
        }
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }
}

package org.abner.manager.repository.empresa.dao;

import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.repository.GenericDAO;
import org.abner.manager.repository.empresa.EmpresaRepository;

import android.content.Context;

public class EmpresaDAO extends GenericDAO<Empresa> implements EmpresaRepository {

    public EmpresaDAO(Context context) {
        super(context);
    }

}

package org.abner.manager.model.empresa;

import org.abner.manager.model.AbstractModel;

@SuppressWarnings("serial")
public class Tipo extends AbstractModel {

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}

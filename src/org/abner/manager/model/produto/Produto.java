package org.abner.manager.model.produto;

import org.abner.manager.model.AbstractModel;

public class Produto extends AbstractModel {

    private Classe classe;

    private String descricao;

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

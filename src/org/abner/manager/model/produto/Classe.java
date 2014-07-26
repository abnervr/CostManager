package org.abner.manager.model.produto;

import org.abner.manager.model.AbstractModel;

public class Classe extends AbstractModel {

    private Classe pai;

    private String descricao;

    private String path;

    public Classe getPai() {
        return pai;
    }

    public void setPai(Classe pai) {
        this.pai = pai;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}

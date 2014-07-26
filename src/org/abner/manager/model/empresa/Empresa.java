package org.abner.manager.model.empresa;

import org.abner.manager.model.AbstractModel;

@SuppressWarnings("serial")
public class Empresa extends AbstractModel {

    private Tipo tipo;

    private String nome;

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}

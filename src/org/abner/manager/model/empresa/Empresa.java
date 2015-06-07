package org.abner.manager.model.empresa;

import org.abner.manager.model.AbstractModel;

@SuppressWarnings("serial")
public class Empresa extends AbstractModel {

    private Tipo tipo;

    private String nome;

    private String identificador;

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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

}

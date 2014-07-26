package org.abner.manager.model.empresa;

import org.abner.manager.model.AbstractModel;

@SuppressWarnings("serial")
public class Telefone extends AbstractModel {

    private Estabelecimento estabelecimento;

    private Integer ddd;

    private String numero;

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public Integer getDdd() {
        return ddd;
    }

    public void setDdd(Integer ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}

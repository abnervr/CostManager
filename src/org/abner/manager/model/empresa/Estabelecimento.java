package org.abner.manager.model.empresa;

import org.abner.manager.model.AbstractModel;
import org.abner.manager.model.posicao.Endereco;

@SuppressWarnings("serial")
public class Estabelecimento extends AbstractModel {

    private Endereco endereco;

    private Empresa empresa;

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

}

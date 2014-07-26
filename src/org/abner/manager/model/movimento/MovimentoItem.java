package org.abner.manager.model.movimento;

import java.math.BigDecimal;
import java.util.Date;

import org.abner.manager.model.AbstractModel;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.empresa.Estabelecimento;
import org.abner.manager.model.produto.Produto;

public class MovimentoItem extends AbstractModel {

    private Movimento movimento;

    private Date data;

    private Empresa empresa;

    private Estabelecimento estabelecimento;

    private Produto produto;

    private BigDecimal valor;

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

}

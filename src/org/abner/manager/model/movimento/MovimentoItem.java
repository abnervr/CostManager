package org.abner.manager.model.movimento;

import java.math.BigDecimal;

import org.abner.manager.model.AbstractModel;
import org.abner.manager.model.produto.Produto;

public class MovimentoItem extends AbstractModel {

    private Movimento movimento;

    private Produto produto;

    private BigDecimal valor;

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
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

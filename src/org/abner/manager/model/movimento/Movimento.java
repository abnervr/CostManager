package org.abner.manager.model.movimento;

import java.math.BigDecimal;
import java.util.Date;

import org.abner.manager.model.AbstractModel;

public class Movimento extends AbstractModel {

    private TipoMovimento tipo;

    private Date data;

    private BigDecimal valor;

    public TipoMovimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimento tipo) {
        this.tipo = tipo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}

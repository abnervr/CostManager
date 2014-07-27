package org.abner.manager.model.movimento;

import java.math.BigDecimal;
import java.util.Date;

import org.abner.manager.model.AbstractModel;
import org.abner.manager.model.empresa.Empresa;
import org.abner.manager.model.empresa.Estabelecimento;

public class Movimento extends AbstractModel {

    private TipoMovimento tipo;

    private Date data;

    private Empresa empresa;

    private Estabelecimento estabelecimento;

    private Movimento movimentoPai;

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

    public Movimento getMovimentoPai() {
        return movimentoPai;
    }

    public void setMovimentoPai(Movimento movimentoPai) {
        this.movimentoPai = movimentoPai;
    }
}

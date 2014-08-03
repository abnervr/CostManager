package org.abner.manager.activities.main.adapter.gastos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Gasto {

    private String periodo;

    private String empresa;

    private String tipo;

    private BigDecimal credito = BigDecimal.ZERO;
    private BigDecimal debito = BigDecimal.ZERO;

    private final transient List<Gasto> gastos = new ArrayList<Gasto>();

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getSaldo() {
        if (credito != null && debito != null) {
            return credito.subtract(debito);
        }
        return null;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void addGasto(Gasto gasto) {
        gastos.add(gasto);
        credito = gasto.getCredito().add(credito);
        debito = gasto.getDebito().add(debito);
    }
}

package org.abner.manager.activities.main.adapter.gastos;

import java.math.BigDecimal;

public class Gasto {

    private String periodo;

    private BigDecimal credito = BigDecimal.ZERO;
    private BigDecimal debito = BigDecimal.ZERO;

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

}

package org.abner.manager.activities.main.adapter.gastos;

public enum Grouping {
    DIA("%Y %m %d"),
    SEMANA("%Y %W "),
    MES("%Y %m"),
    ANO("%Y");

    private String format;

    private Grouping(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}

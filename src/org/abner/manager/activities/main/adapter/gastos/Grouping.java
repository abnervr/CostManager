package org.abner.manager.activities.main.adapter.gastos;

public enum Grouping {
    DIA("%Y %m %d", "Dia"),
    SEMANA("%Y %W ", "Semana"),
    MES("%Y %m", "Mês"),
    ANO("%Y", "Ano");

    private String format;
    private String title;

    private Grouping(String format, String title) {
        this.format = format;
        this.title = title;
    }

    public String format(String value) {
        GroupingFormat groupingFormat = GroupingFormat.getGroupingInstance(this);
        if (groupingFormat != null) {
            return groupingFormat.format(value);
        } else {
            return value;
        }
    }

    public String getFormat() {
        return format;
    }

    public String getTitle() {
        return title;
    }
}

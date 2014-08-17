package org.abner.manager.activities.main.adapter.gastos;

public enum Grouping {
    DIA("%Y %m %d", "Dia"),
    SEMANA("%Y %W ", "Semana"),
    MES("%Y %m", "Mês"),
    ANO("%Y", "Ano");

    private String format;
    private String title;
    private GroupingFormat groupingFormat;

    private Grouping(String format, String title) {
        this.format = format;
        this.title = title;
        this.groupingFormat = GroupingFormat.getGroupingInstance(this);
    }

    public String format(String value) {
        if (groupingFormat != null) {
            return value;
        } else {
            return groupingFormat.format(value);
        }
    }

    public String getFormat() {
        return format;
    }

    public String getTitle() {
        return title;
    }
}

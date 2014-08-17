package org.abner.manager.activities.main.adapter.gastos;

public abstract class GroupingFormat {

    public static GroupingFormat getGroupingInstance(Grouping grouping) {
        switch (grouping) {
            case MES:
                return new MesGroupingFormat();
            case DIA:
                return new DiaGroupingFormat();
            case ANO:
            case SEMANA:
            default:
                return null;
        }
    }

    public abstract String format(String value);
}

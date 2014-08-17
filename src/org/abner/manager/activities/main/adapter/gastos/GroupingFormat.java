package org.abner.manager.activities.main.adapter.gastos;

public abstract class GroupingFormat {

    private static final DiaGroupingFormat DIA_GROUPING_FORMAT = new DiaGroupingFormat();
    private static final MesGroupingFormat MES_GROUPING_FORMAT = new MesGroupingFormat();

    public static GroupingFormat getGroupingInstance(Grouping grouping) {
        switch (grouping) {
            case MES:
                return MES_GROUPING_FORMAT;
            case DIA:
                return DIA_GROUPING_FORMAT;
            case ANO:
            case SEMANA:
            default:
                return null;
        }
    }

    public abstract String format(String value);
}

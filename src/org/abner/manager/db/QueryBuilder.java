package org.abner.manager.db;

import java.util.List;

import org.abner.manager.db.model.Table;

public class QueryBuilder {

    private final Table<?> table;
    private StringBuilder sb;

    public <T> QueryBuilder(Class<T> model) {
        this.table = new Table<T>(model);
    }

    public String buildQuery(String where, String order) {
        sb = new StringBuilder();

        appendColumns();
        appendFrom();
        appendWhere(where);
        appendOrderBy(order);

        return sb.toString();
    }

    private void appendColumns() {
        sb.append("select ");

        boolean first = true;
        for (String column : table.getColumnNames()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(column);
        }
        appendDependences(table.getDependences());
    }

    private void appendDependences(List<Table<?>> dependences) {
        for (Table<?> dependence : dependences) {
            for (String column : dependence.getColumnNames()) {
                sb.append(", ");
                sb.append(dependence.getName());
                sb.append(".");
                sb.append(column);

                sb.append(" as ");
                sb.append(dependence.getName());
                sb.append("_");
                sb.append(column);

            }
        }
    }

    private void appendFrom() {
        sb.append(" from ");
        sb.append(table.getName());
        for (Table<?> dependence : table.getDependences()) {
            sb.append(" left outer join ");
            sb.append(dependence.getName());
            sb.append(" on ");
            sb.append(table.getName());
            sb.append(".");
            //TODO
            sb.append("");
            sb.append(" = ");
            sb.append(dependence.getName());
            sb.append(".id");
        }
    }

    private void appendWhere(String where) {
        if (where != null) {
            sb.append(" where ");
            sb.append(where);
        }
    }

    private void appendOrderBy(String order) {
        if (order != null) {
            sb.append(" order by ");
            sb.append(order);
        }
    }

}

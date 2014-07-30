package org.abner.manager.db;

import java.util.ArrayList;
import java.util.List;

import org.abner.manager.db.model.ModelProperties;

public class QueryBuilder {

    private final Class<?> model;
    private final List<Class<?>> dependences = new ArrayList<Class<?>>();
    private StringBuilder sb;

    public QueryBuilder(Class<?> model) {
        this.model = model;
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
        for (String column : ModelProperties.getColumnNames(model)) {
            sb.append(column);
        }
        sb.append(' ');
    }

    private void appendFrom() {
        sb.append("from ");
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

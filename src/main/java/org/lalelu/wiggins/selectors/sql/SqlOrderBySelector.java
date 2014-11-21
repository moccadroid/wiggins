package org.lalelu.wiggins.selectors.sql;

public class SqlOrderBySelector<T> extends SqlDefaultSelector<T> {
    private String orderField = null;

    public SqlOrderBySelector(String order, String direction) {
        if(!order.isEmpty())
            orderField = " ORDER BY " + order + " " + direction;
    }

    @Override
    public String orderField() {
        return orderField;
    }
}

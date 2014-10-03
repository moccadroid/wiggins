package org.lalelu.wiggins.selectors.sql;

public class OrderBySelector<T> extends DefaultSelector<T> {
    private String orderField = null;

    public OrderBySelector(String order, String direction) {
        if(!order.isEmpty())
            orderField = " ORDER BY " + order + " " + direction;
    }

    @Override
    public String orderField() {
        return orderField;
    }
}

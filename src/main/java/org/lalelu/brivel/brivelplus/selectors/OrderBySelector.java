package org.lalelu.brivel.brivelplus.selectors;

public class OrderBySelector extends DefaultSelector {
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

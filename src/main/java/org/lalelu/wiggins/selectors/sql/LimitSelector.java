package org.lalelu.wiggins.selectors.sql;

public class LimitSelector<T> extends DefaultSelector<T> {
    private String limitField = null;

    public LimitSelector(String limit) {
        if(limit.isEmpty())
            limitField = null;
        else
            limitField = " LIMIT " + limit;
    }

    @Override
    public String limitField() {
        return limitField;
    }

}

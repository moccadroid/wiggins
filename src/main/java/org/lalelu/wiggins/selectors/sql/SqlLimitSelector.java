package org.lalelu.wiggins.selectors.sql;

public class SqlLimitSelector<T> extends SqlDefaultSelector<T> {
    private String limitField = null;

    public SqlLimitSelector(String limit) {
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

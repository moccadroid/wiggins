package org.lalelu.wiggins.selectors.sql;

import java.util.List;

public class WhereSelector<T> extends DefaultSelector<T> {
    private String whereField = null;
    private List<?> compareList = null;

    public WhereSelector(String whereField) {
        this.whereField = whereField;
    }

    public WhereSelector(String whereField, List<?> compareList) {
        if(compareList.isEmpty())
            this.whereField = "";
        else
            this.whereField = whereField;

        this.compareList = compareList;
    }

    @Override
    public String whereField() {
        if(compareList != null && compareList.size() > 0) {
            String where = whereField + "(";
            for (Object object : compareList) {
                where += object + ",";
            }
            where = where.substring(0, where.length() - 1);
            where += ")";
            return where;
        }
        return whereField;
    }
}

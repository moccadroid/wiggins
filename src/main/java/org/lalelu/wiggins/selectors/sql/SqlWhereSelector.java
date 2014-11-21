package org.lalelu.wiggins.selectors.sql;

import java.util.List;

public class SqlWhereSelector<T> extends SqlDefaultSelector<T> {
    private String whereField = null;
    private List<?> compareList = null;

    public SqlWhereSelector(String whereField) {
        this.whereField = whereField;
    }

    public SqlWhereSelector(String whereField, List<?> compareList) {
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

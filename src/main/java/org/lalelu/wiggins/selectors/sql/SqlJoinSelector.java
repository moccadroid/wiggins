package org.lalelu.wiggins.selectors.sql;

public class SqlJoinSelector<T> extends SqlDefaultSelector<T> {
    private String tableField = null;
    private String leftJoinSide = null;
    private String rightJoinSide = null;

    public SqlJoinSelector(String tableField, String leftJoinSide, String rightJoinSide) {
        this.tableField = tableField;
        this.leftJoinSide = leftJoinSide;
        this.rightJoinSide = rightJoinSide;
    }

    @Override
    public String tableField() {
        return tableField;
    }

    @Override
    public String leftJoinSide() {
        return leftJoinSide;
    }

    @Override
    public String rightJoinSide() {
        return rightJoinSide;
    }
}

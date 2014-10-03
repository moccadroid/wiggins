package org.lalelu.wiggins.selectors.sql;

public class JoinSelector<T> extends DefaultSelector<T> {
    private String tableField = null;
    private String leftJoinSide = null;
    private String rightJoinSide = null;

    public JoinSelector(String tableField, String leftJoinSide, String rightJoinSide) {
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

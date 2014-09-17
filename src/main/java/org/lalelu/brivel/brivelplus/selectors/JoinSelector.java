package org.lalelu.brivel.brivelplus.selectors;

public class JoinSelector extends DefaultSelector {
    private String tableField = "";
    private String fkString = "";

    public JoinSelector(String tableField, String fkString) {
        this.tableField = tableField;
        this.fkString = fkString;
    }

    @Override
    public String selectField() {
        return null;
    }

    @Override
    public String tableField() {
        return tableField;
    }

    @Override
    public String joinField() {
        return fkString;
    }
}

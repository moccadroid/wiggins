package org.lalelu.wiggins.conditions;

public class BreakCondition implements Condition {
    private String field = "";
    private Object object = null;

    public BreakCondition(String field, String object) {
        this.field = field;
        this.object = object;
    }

    public String getField() {
        return field;
    }

    @Override
    public boolean test(Object testObject) {
        if(object.equals(testObject))
            return true;
        return false;
    }
}

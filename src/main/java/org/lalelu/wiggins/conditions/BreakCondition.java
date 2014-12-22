package org.lalelu.wiggins.conditions;

import org.lalelu.wiggins.selectors.Selector;

public class BreakCondition implements Condition {
    protected String field = "";
    protected Object object = null;
    protected boolean ifEqual = true;
    protected Selector selector = null;

    public BreakCondition(Selector selector, Object value, boolean ifEqual) {
        this.selector = selector;
        this.object = value;
        this.ifEqual = ifEqual;
    }

    public BreakCondition(String field, String object, boolean ifEqual) {
        this.field = field;
        this.object = object;
        this.ifEqual = ifEqual;
    }

    public String getField() {
        return field;
    }

    @Override
    public boolean test(Object testObject) {
        if(this.ifEqual) {
            if (object.equals(testObject))
                return true;
        } else {
            if(!object.equals(testObject))
                return true;
        }
        return false;
    }
}

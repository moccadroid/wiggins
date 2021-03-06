package org.lalelu.wiggins.selectors.dataconverter;

public class BooleanDataConverter extends DefaultDataConverter {
    public Object read(Object object) {
        if(object == null)
            return false;
        if(object instanceof String) {
            if(((String)object).trim().equals("true"))
                return true;
        }
        return false;
    }
}

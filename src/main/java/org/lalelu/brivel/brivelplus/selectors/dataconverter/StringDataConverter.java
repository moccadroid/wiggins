package org.lalelu.brivel.brivelplus.selectors.dataconverter;

public class StringDataConverter extends DefaultDataConverter {
    @Override
    public Object read(Object object) {
        return "" + object;
    }
    @Override
    public Object write(Object object) {
        if(object == null)
            return "''";
        return "'"+object+"'";
    }
}

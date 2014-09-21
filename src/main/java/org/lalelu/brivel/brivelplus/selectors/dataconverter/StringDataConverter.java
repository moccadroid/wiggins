package org.lalelu.brivel.brivelplus.selectors.dataconverter;

public class StringDataConverter extends DefaultDataConverter {
    @Override
    public String read(Object object) {
        return String.valueOf(object);
    }
    @Override
    public String write(Object object) {
        if(object == null)
            return "''";
        return "'"+object+"'";
    }
}

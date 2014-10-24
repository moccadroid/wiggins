package org.lalelu.wiggins.selectors.dataconverter;

public class DefaultValueConverter extends DefaultDataConverter {
    private Object defaultValue = null;

    public DefaultValueConverter(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object read(Object object) {
        return defaultValue;
    }

}

package org.lalelu.wiggins.selectors.csv.dataconverter;

public class DefaultValueConverter extends DefaultDataConverter {
    private Object defaultValue = null;

    public DefaultValueConverter(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Object read(Object object) {
        return defaultValue;
    }
}

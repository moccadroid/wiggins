package org.lalelu.wiggins.selectors.dataconverter;

public class DoubleDataConverter extends DefaultDataConverter {
    @Override
    public Double read(Object object) {
        return Double.valueOf(String.valueOf(object));
    }
}

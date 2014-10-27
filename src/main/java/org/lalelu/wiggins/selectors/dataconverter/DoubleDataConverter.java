package org.lalelu.wiggins.selectors.dataconverter;

public class DoubleDataConverter extends DefaultDataConverter {
    @Override
    public Double read(Object object) {
        if(object == null)
            return 0.0;
        return Double.valueOf(String.valueOf(object));
    }
}

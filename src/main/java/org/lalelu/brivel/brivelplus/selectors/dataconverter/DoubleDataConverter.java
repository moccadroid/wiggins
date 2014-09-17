package org.lalelu.brivel.brivelplus.selectors.dataconverter;

public class DoubleDataConverter extends DefaultDataConverter {
    @Override
    public Object read(Object object) {
        return Double.parseDouble("" + object);
    }
}

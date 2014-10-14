package org.lalelu.wiggins.selectors.csv.dataconverter;

public class DefaultDataConverter implements DataConverter {
    @Override
    public Object read(Object object) {
        return object;
    }

    @Override
    public Object readCombined(Object firstObject, Object secondObject) {
        return firstObject;
    }
}

package org.lalelu.wiggins.selectors.csv.dataconverter;

public interface DataConverter {
    public Object read(Object object);

    public Object readCombined(Object firstObject, Object secondObject);
}

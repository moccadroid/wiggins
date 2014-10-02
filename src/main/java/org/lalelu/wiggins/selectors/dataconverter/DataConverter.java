package org.lalelu.wiggins.selectors.dataconverter;

public interface DataConverter {
    public Object read(Object object);
    public Object write(Object object);
}

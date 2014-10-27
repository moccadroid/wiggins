package org.lalelu.wiggins.selectors;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;

public interface Selector {
    public String getObjectField();
    public String getSelectorPath();
    public DataConverter getDataConverter();
    public Class<?> getFieldType();
    public String getPrefix();
}

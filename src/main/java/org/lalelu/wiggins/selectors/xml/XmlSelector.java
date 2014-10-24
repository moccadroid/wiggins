package org.lalelu.wiggins.selectors.xml;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;

public interface XmlSelector {
    public String getSelectorPath();
    public String getObjectField();
    public DataConverter getDataConverter();
    public String getAttribute();
    public Class<?> getFieldType();
}

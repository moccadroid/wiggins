package org.lalelu.wiggins.selectors.json;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;

public interface JsonSelector {

    public String getObjectField();
    public String getSelectorPath();
    public DataConverter getDataConverter(Class<?> klass);
    public Class<?> getFieldType();
}

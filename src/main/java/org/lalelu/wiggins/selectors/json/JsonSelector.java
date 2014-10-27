package org.lalelu.wiggins.selectors.json;

import org.lalelu.wiggins.selectors.Selector;
import org.lalelu.wiggins.selectors.dataconverter.DataConverter;

public interface JsonSelector extends Selector {
    public DataConverter getDataConverter(Class<?> klass);
}

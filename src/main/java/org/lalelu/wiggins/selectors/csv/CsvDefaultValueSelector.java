package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.DefaultValueSelector;
import org.lalelu.wiggins.selectors.dataconverter.DefaultValueConverter;

public class CsvDefaultValueSelector extends CsvFieldSelector implements DefaultValueSelector {
    public CsvDefaultValueSelector(String objectField, Class<?> fieldType, Object defaultValue) {
        super(null,objectField, "", fieldType, new DefaultValueConverter(defaultValue));
    }
}

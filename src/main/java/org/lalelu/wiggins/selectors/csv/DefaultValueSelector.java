package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.csv.dataconverter.DefaultValueConverter;

public class DefaultValueSelector extends CsvSelector {
    public DefaultValueSelector(String objectField, Class fieldType, Object defaultValue) {
        super(null,objectField, fieldType, new DefaultValueConverter(defaultValue));
    }
}

package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.csv.dataconverter.DefaultValueConverter;

public class CsvDefaultValueSelector extends CsvFieldSelector {
    public CsvDefaultValueSelector(String objectField, Class fieldType, Object defaultValue) {
        super(null,objectField, fieldType, new DefaultValueConverter(defaultValue));
    }
}

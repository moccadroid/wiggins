package org.lalelu.wiggins.selectors.sql;

import org.lalelu.wiggins.selectors.dataconverter.DefaultValueConverter;

public class SqlDefaultValueSelector extends SqlFieldSelector {
    public SqlDefaultValueSelector(String objectField, Class<?> fieldType, Object defaultValue) {
        super(null, null, objectField, fieldType, new DefaultValueConverter(defaultValue));
    }
}

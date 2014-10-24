package org.lalelu.wiggins.selectors.json;

import org.lalelu.wiggins.selectors.dataconverter.DefaultValueConverter;

public class JsonDefaultValueSelector extends JsonFieldSelector {

    public JsonDefaultValueSelector(String objectField, Class<?> fieldType, Object defaultValue) {
        super("", objectField, fieldType, new DefaultValueConverter(defaultValue));
    }
}

package org.lalelu.wiggins.selectors.json;

import org.lalelu.wiggins.selectors.DefaultValueSelector;
import org.lalelu.wiggins.selectors.dataconverter.DefaultValueConverter;

public class JsonDefaultValueSelector extends JsonFieldSelector implements DefaultValueSelector {

    public JsonDefaultValueSelector(String objectField, Class<?> fieldType, Object defaultValue) {
        super("", objectField, fieldType, new DefaultValueConverter(defaultValue));
    }
}

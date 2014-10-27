package org.lalelu.wiggins.selectors.json;

import org.lalelu.wiggins.selectors.dataconverter.CombinedDataConverter;

public class JsonCombinedFieldSelector extends JsonFieldSelector {
    public JsonCombinedFieldSelector(String selectorPath, String objectField, String separator, Class<?> fieldType) {
        super(selectorPath, objectField, fieldType, new CombinedDataConverter(separator));
    }
}

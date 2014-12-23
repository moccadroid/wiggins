package org.lalelu.wiggins.selectors.xml;

import org.lalelu.wiggins.selectors.DefaultValueSelector;
import org.lalelu.wiggins.selectors.dataconverter.DefaultValueConverter;

public class XmlDefaultValueSelector extends XmlFieldSelector implements DefaultValueSelector {
    public XmlDefaultValueSelector(String objectField, Class<?> fieldType, String defaultValue) {
        super(null, objectField, fieldType, new DefaultValueConverter(defaultValue));
    }
}

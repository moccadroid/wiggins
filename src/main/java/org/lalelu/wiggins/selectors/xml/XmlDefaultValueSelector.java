package org.lalelu.wiggins.selectors.xml;

import org.lalelu.wiggins.selectors.dataconverter.DefaultValueConverter;

public class XmlDefaultValueSelector extends XmlFieldSelector {
    public XmlDefaultValueSelector(String objectField, Class<?> fieldType, String defaultValue) {
        super(null, objectField, null, fieldType, new DefaultValueConverter(defaultValue));
    }
}

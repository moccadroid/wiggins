package org.lalelu.wiggins.selectors.xml;

import org.lalelu.wiggins.selectors.dataconverter.CombinedDataConverter;

public class XmlCombinedFieldSelector extends XmlFieldSelector {
    public XmlCombinedFieldSelector(String selectorPath, String objectField, String separator, Class<?> fieldType) {
        super(selectorPath, objectField, fieldType, new CombinedDataConverter(separator));
    }
}

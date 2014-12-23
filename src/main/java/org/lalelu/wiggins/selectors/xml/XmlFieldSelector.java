package org.lalelu.wiggins.selectors.xml;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.dataconverter.provider.DataConverterProvider;

public class XmlFieldSelector implements XmlSelector {
    private String selectorPath = "";
    private String objectField = "";
    private DataConverter dataConverter = null;
    private String attribute = "";
    private Class<?> fieldType = null;

    public XmlFieldSelector(String selectorPath, String objectField, Class<?> fieldType) {
        this(selectorPath, objectField, fieldType, DataConverterProvider.getDefaultDataConverter(fieldType));
    }

    public XmlFieldSelector(String selectorPath, String objectField, Class<?> fieldType, DataConverter dataConverter) {
        if(selectorPath != null && selectorPath.contains("::")) {
            String[] split = selectorPath.split("::");
            if(split.length > 1) {
                this.attribute = split[1];
                this.selectorPath = split[0];
            }
        } else {
            this.selectorPath = selectorPath;
        }

        this.objectField = objectField.substring(0,1).toUpperCase() + objectField.substring(1);
        this.dataConverter = dataConverter;
        this.fieldType = fieldType;
    }

    public String getSelectorPath() {
        return this.selectorPath;
    }

    public String getObjectField() {
        return this.objectField;
    }

    public Class<?> getFieldType() {
        return this.fieldType;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    public String getAttribute() {
        return this.attribute;
    }

    public DataConverter getDataConverter() {
        return this.dataConverter;
    }
}

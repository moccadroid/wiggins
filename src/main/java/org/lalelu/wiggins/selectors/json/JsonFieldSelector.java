package org.lalelu.wiggins.selectors.json;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.dataconverter.DefaultDataConverter;
import org.lalelu.wiggins.selectors.dataconverter.provider.DataConverterProvider;

public class JsonFieldSelector implements JsonSelector {
    private String selectorPath = "";
    private String objectField = "";
    private DataConverter dataConverter = null;
    private Class<?> fieldType = null;

    public JsonFieldSelector(String path) {
        String[] parts = path.split(":");
        char firstLetter = Character.toUpperCase(parts[0].charAt(0));
        this.objectField = firstLetter + parts[0].substring(1);
        this.selectorPath = parts[1];
    }

    public JsonFieldSelector(String selectorPath, String objectField, Class<?> fieldType, DataConverter dataConverter) {
        this.selectorPath = selectorPath;
        this.objectField = objectField.substring(0,1).toUpperCase() + objectField.substring(1);
        this.dataConverter = dataConverter;
        this.fieldType = fieldType;
    }

    public String getObjectField() {
        return this.objectField;
    }

    public String getSelectorPath() {
        return this.selectorPath;
    }

    @Override
    public DataConverter getDataConverter() {
        if(fieldType != null)
            return DataConverterProvider.getDefaultDataConverter(fieldType);
        return new DefaultDataConverter();
    }

    @Override
    public DataConverter getDataConverter(Class<?> klass) {
        if(this.dataConverter == null)
            return DataConverterProvider.getDefaultDataConverter(klass);
        return this.dataConverter;
    }

    @Override
    public Class<?> getFieldType() {
        if(this.fieldType == null)
            return Object.class;
        return fieldType;
    }

    @Override
    public String getPrefix() {
        return null;
    }

    public String toString() {
        String toString = "";
        toString += "selectorPath: " + this.selectorPath + "\n";
        toString += "field: " + this.objectField + "\n";
        return toString;
    }
}

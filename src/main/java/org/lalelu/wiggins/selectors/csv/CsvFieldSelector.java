package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.dataconverter.DefaultDataConverter;
import org.lalelu.wiggins.selectors.dataconverter.provider.DataConverterProvider;

public class CsvFieldSelector implements CsvSelector {
    private String csvField = "";
    private String objectField = "";
    private Class<?> fieldType = null;
    private DataConverter dataConverter = null;
    private String prefix = "";

    public CsvFieldSelector(String csvField, String objectField, Class<?> fieldType) {
        this(csvField, (objectField.substring(0,1).toUpperCase() + objectField.substring(1)), "", fieldType, DataConverterProvider.getDefaultDataConverter(fieldType));
    }

    public CsvFieldSelector(String csvField, String objectField, Class<?> fieldType, DataConverter dataConverter) {
        this(csvField, (objectField.substring(0,1).toUpperCase() + objectField.substring(1)), "", fieldType, dataConverter);
    }

    public CsvFieldSelector(String csvField, String objectField, String prefix, Class<?> fieldType) {
        this(csvField, (objectField.substring(0,1).toUpperCase() + objectField.substring(1)), prefix, fieldType, DataConverterProvider.getDefaultDataConverter(fieldType));
    }

    public CsvFieldSelector(String csvField, String objectField, String prefix, Class<?> fieldType, DataConverter dataConverter) {
        if(!Character.isUpperCase(objectField.charAt(0)))
            objectField = objectField.substring(0,1).toUpperCase() + objectField.substring(1);

        this.objectField = objectField;
        this.csvField = csvField;
        this.fieldType = fieldType;
        this.dataConverter = dataConverter;
        this.prefix = prefix;
    }

    @Override
    public DataConverter getDataConverter() {
        return dataConverter;
    }

    @Override
    public String getSelectorPath() {
        return csvField;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getObjectField() {
        return objectField;
    }

    @Override
    public Class<?> getFieldType() {
        return fieldType;
    }

}

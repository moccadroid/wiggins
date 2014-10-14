package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.csv.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.csv.dataconverter.DefaultDataConverter;

public class CsvSelector {
    private String csvField = "";
    private String objectField = "";
    private Class fieldType = null;
    private DataConverter dataConverter = null;
    private String prefix = "";

    public CsvSelector(String csvField, String objectField, Class fieldType) {
        this(csvField, (objectField.substring(0,1).toUpperCase() + objectField.substring(1)), fieldType, new DefaultDataConverter());
    }

    public CsvSelector(String csvField, String objectField, String prefix, Class fieldType) {
        this(csvField, (objectField.substring(0,1).toUpperCase() + objectField.substring(1)), fieldType, new DefaultDataConverter());

        this.prefix = prefix;
    }

    public CsvSelector(String csvField, String objectField, Class fieldType, DataConverter dataConverter) {
        if(!Character.isUpperCase(objectField.charAt(0)))
            objectField = objectField.substring(0,1).toUpperCase() + objectField.substring(1);

        this.objectField = objectField;
        this.csvField = csvField;
        this.fieldType = fieldType;
        this.dataConverter = dataConverter;
    }

    public DataConverter getDataConverter() {
        return dataConverter;
    }

    public String getCsvField() {
        return csvField;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getObjectField() {
        return objectField;
    }

    public Class getFieldType() {
        return fieldType;
    }
}

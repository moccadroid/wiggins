package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.csv.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.csv.dataconverter.DefaultDataConverter;

public class CsvIndexSelector implements CsvSelector {
    private Integer index;
    private String objectField;
    private DataConverter dataConverter = null;
    private Class<?> fieldType = null;

    public CsvIndexSelector(Integer index, String objectField, Class<?> fieldType) {
        this(index, (objectField.substring(0,1).toUpperCase() + objectField.substring(1)), fieldType, new DefaultDataConverter());
    }

    public CsvIndexSelector(Integer index, String objectField, Class<?> fieldType, DataConverter dataConverter) {
        if(!Character.isUpperCase(objectField.charAt(0)))
            objectField = objectField.substring(0,1).toUpperCase() + objectField.substring(1);

        this.index = index;
        this.objectField = objectField;
        this.fieldType = fieldType;
        this.dataConverter = dataConverter;
    }

    @Override
    public DataConverter getDataConverter() {
        return dataConverter;
    }

    @Override
    public String getCsvField() {
        return "" + index;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public String getObjectField() {
        return objectField;
    }

    @Override
    public Class getFieldType() {
        return fieldType;
    }
}

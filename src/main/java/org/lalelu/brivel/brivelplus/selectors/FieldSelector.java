package org.lalelu.brivel.brivelplus.selectors;

import org.lalelu.brivel.brivelplus.selectors.dataconverter.DataConverter;
import org.lalelu.brivel.brivelplus.selectors.dataconverter.provider.DataConverterProvider;

public class FieldSelector<T> extends DefaultSelector<T> {
    private String selectField = null;
    private String tableField = null;
    private String fieldName = null;
    private Class<T> klass = null;
    private String alias = "";

    public FieldSelector(String selectField, String tableField, Class<T> klass) {
        this(selectField, tableField, (selectField.substring(0, 1).toUpperCase() + selectField.substring(1)), klass, DataConverterProvider.getDefaultDataConverter(klass));
    }

    public FieldSelector(String selectField, String tableField, String fieldName, Class<T> klass) {
        this(selectField, tableField, fieldName, klass, DataConverterProvider.getDefaultDataConverter(klass));
    }

    public FieldSelector(String selectField, String tableField, String fieldName, Class<T> klass, DataConverter dataConverter) {
        this.selectField = selectField;
        this.tableField = tableField;
        this.fieldName = fieldName;
        this.klass = klass;

        // generate an alias
        alias = tableField + "_" + selectField;

        this.dataConverter = dataConverter;
    }

    @Override
    public String selectField() {
        return selectField;
    }

    @Override
    public String tableField() {
        return tableField;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Class<T> getType() {
        return klass;
    }

}

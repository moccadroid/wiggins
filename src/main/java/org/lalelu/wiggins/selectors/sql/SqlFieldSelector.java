package org.lalelu.wiggins.selectors.sql;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.dataconverter.provider.DataConverterProvider;

public class SqlFieldSelector<T> extends SqlDefaultSelector<T> {
    private String selectField = null;
    private String tableField = null;
    private String fieldName = null;
    private Class<T> klass = null;
    private String alias = "";
    private boolean key = false;

    public SqlFieldSelector(String selectField, String tableField, Class<T> klass) {
        this(selectField, tableField, "set" + (selectField.substring(0, 1).toUpperCase() + selectField.substring(1)), klass, DataConverterProvider.getDefaultDataConverter(klass));
    }

    public SqlFieldSelector(String selectField, String tableField, String fieldName, Class<T> klass) {
        this(selectField, tableField, fieldName, klass, DataConverterProvider.getDefaultDataConverter(klass));
    }

    public SqlFieldSelector(String selectField, String tableField, String fieldName, Class<T> klass, DataConverter dataConverter) {
        this.selectField = selectField;
        this.tableField = tableField;
        this.fieldName = fieldName;
        this.klass = klass;

        // generate an alias
        alias = tableField + "_" + selectField;

        this.dataConverter = dataConverter;
    }

    @Override
    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean isKey() {
        return key;
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

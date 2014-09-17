package org.lalelu.brivel.brivelplus.selectors;

import org.lalelu.brivel.brivelplus.selectors.dataconverter.DataConverter;
import org.lalelu.brivel.brivelplus.selectors.dataconverter.DefaultDataConverter;

import java.util.Map;

public class DefaultSelector implements Selector {
    protected DataConverter dataConverter = new DefaultDataConverter();

    @Override
    public String selectField() {
        return null;
    }

    @Override
    public String tableField() {
        return null;
    }

    @Override
    public String whereField() {
        return null;
    }

    @Override
    public String joinField() {
        return null;
    }

    @Override
    public String limitField() {
        return null;
    }

    @Override
    public String orderField() {
        return null;
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public String getFieldName() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public Class getType() {
        return null;
    }

    @Override
    public DataConverter getDataConverter() {
        return dataConverter;
    }

    @Override
    public void setDataConverter(DataConverter dataConverter) {
        this.dataConverter = dataConverter;
    }
}

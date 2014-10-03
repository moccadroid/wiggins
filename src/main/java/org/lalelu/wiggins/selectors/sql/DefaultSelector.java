package org.lalelu.wiggins.selectors.sql;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.dataconverter.DefaultDataConverter;

import java.util.Map;

public class DefaultSelector<T> implements Selector<T> {
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
    public String leftJoinSide() {
        return null;
    }

    @Override
    public String rightJoinSide() {
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
    public boolean isKey() {
        return false;
    }

    @Override
    public void setKey(boolean key) {

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
    public Class<T> getType() {
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

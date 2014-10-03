package org.lalelu.wiggins.selectors.sql;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;

import java.util.Map;

public interface Selector<T> {
    public String selectField();
    public String tableField();
    public String whereField();
    public String leftJoinSide();
    public String rightJoinSide();
    public String limitField();
    public String orderField();

    public boolean isKey();
    public void setKey(boolean key);

    public Map<String, Object> getParameters();

    public String getFieldName();

    public String getAlias();

    public Class<T> getType();

    public DataConverter getDataConverter();
    public void setDataConverter(DataConverter dataConverter);
}

package org.lalelu.brivel.brivelplus.selectors;

import org.lalelu.brivel.brivelplus.selectors.dataconverter.DataConverter;

import java.util.Map;

public interface Selector<E> {
    public String selectField();
    public String tableField();
    public String whereField();
    public String leftJoinSide();
    public String rightJoinSide();
    public String limitField();
    public String orderField();

    public Map<String, Object> getParameters();

    public String getFieldName();

    public String getAlias();

    public Class<E> getType();

    public DataConverter getDataConverter();
    public void setDataConverter(DataConverter dataConverter);
}

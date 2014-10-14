package org.lalelu.wiggins.requests.csv;

import org.lalelu.wiggins.selectors.csv.CsvCombinedFieldSelector;
import org.lalelu.wiggins.selectors.csv.CsvSelector;
import org.lalelu.wiggins.selectors.csv.DefaultValueSelector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CsvObjectModel {
    private Class klass = null;
    //private Map<String, CsvSelector> selectorMap = new LinkedHashMap<String, CsvSelector>();
    private Map<String, List<CsvSelector>> selectorMap = new LinkedHashMap<String, List<CsvSelector>>();
    private Integer objectIndex = 0;
    private Integer objectLength = 0;
    private Object currentObject = null;

    private List<CsvSelector> defaultValueSelectors = new ArrayList<CsvSelector>();

    private String field = "";
    private CsvObjectModel parent = null;

    public CsvObjectModel(Class klass) {
        this.klass = klass;
    }

    public Class getKlass() {
        return klass;
    }

    public void addSelector(CsvSelector selector) {
        if(selector instanceof DefaultValueSelector) {
            defaultValueSelectors.add(selector);
        } else {
            if(!selectorMap.containsKey(selector.getCsvField())) {
                objectLength += 1;
                List<CsvSelector> list = new LinkedList<CsvSelector>();
                list.add(selector);
                selectorMap.put(selector.getCsvField(), list);
            } else {
                selectorMap.get(selector.getCsvField()).add(selector);
            }
        }
    }

    public void setParent(String field, CsvObjectModel parent) {
        this.field = field;
        this.parent = parent;
    }

    public CsvObjectModel getParent() {
        return parent;
    }

    public void createObject() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(objectIndex.equals(0)) {
            currentObject = klass.newInstance();

            for (CsvSelector selector : defaultValueSelectors) {
                Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
                method.invoke(currentObject, selector.getDataConverter().read(null));
            }
        }
    }

    public Object getCurrentObject() {
        return currentObject;
    }

    public Integer getObjectIndex() {
        return objectIndex;
    }

    public String getField() {
        return field;
    }

    public boolean containsObjectField(String csvField) {
        return selectorMap.containsKey(csvField);
    }

    public Object assembleObject(String csvHeader, String csvField) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<CsvSelector> list = selectorMap.get(csvHeader);
        if(list != null) {
            for(CsvSelector selector : list) {
                if(selector instanceof CsvCombinedFieldSelector) {
                    Method getMethod = klass.getMethod("get" + selector.getObjectField());
                    Object currentValue = getMethod.invoke(currentObject);

                    Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
                    method.invoke(currentObject, selector.getDataConverter().readCombined(currentValue, (selector.getPrefix() + csvField)));
                } else {
                    Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
                    method.invoke(currentObject, selector.getDataConverter().read(selector.getPrefix() + csvField));
                }
            }

            objectIndex += 1;
            if(objectIndex.equals(objectLength)) {
                objectIndex = 0;
            }
        }




        return currentObject;
    }
}

package org.lalelu.wiggins.requests.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.lalelu.wiggins.selectors.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.dataconverter.provider.DataConverterProvider;
import org.lalelu.wiggins.selectors.json.JsonDefaultValueSelector;
import org.lalelu.wiggins.selectors.json.JsonFieldSelector;
import org.lalelu.wiggins.selectors.json.JsonSelector;

public class JsonObjectModel {
    private Class<?> klass = null;

    private Integer objectIndex = 0;
    private Integer objectLength = 0;
    private boolean isInObject = false;
    private Object currentObject = null;

    private String field = "";
    private JsonObjectModel parent = null;

    private List<JsonSelector> selectorList = new ArrayList<JsonSelector>();
    private Map<String, List<JsonSelector>> selectorMap = new HashMap<String, List<JsonSelector>>();
    private List<JsonSelector> defaultValueSelectors = new ArrayList<JsonSelector>();

    public JsonObjectModel(Class<?> klass) {
        this.klass = klass;
    }

    public void addSelector(JsonSelector selector) {
        if(selector instanceof JsonDefaultValueSelector) {
            defaultValueSelectors.add(selector);
        } else {
            if (selectorMap.containsKey(selector.getSelectorPath())) {
                selectorMap.get(selector.getSelectorPath()).add(selector);
            } else {
                objectLength += 1;
                List<JsonSelector> list = new ArrayList<JsonSelector>();
                list.add(selector);
                selectorMap.put(selector.getSelectorPath(), list);
            }
        }
        selectorList.add(selector);
    }

    public List<JsonSelector> getSelectors() {
        return selectorList;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public void createObject() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        currentObject = klass.newInstance();
        isInObject = false;

        for(JsonSelector selector : defaultValueSelectors) {
            Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
            method.invoke(currentObject, selector.getDataConverter(null).read(null));
        }
    }

    public void setParent(String field, JsonObjectModel parent) {
        this.field = field;
        this.parent = parent;
    }

    public JsonObjectModel getParent() {
        return parent;
    }

    public Object getCurrentObject() {
        return currentObject;
    }

    public Integer getObjectIndex() {
        return objectIndex;
    }

    public boolean isInObject() {
        return isInObject;
    }

    public String getParentField() {
        return this.field;
    }

    public Object assembleObject(Class<?> targetClass, String path, Object object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<JsonSelector> list = selectorMap.get(path);

        if(list != null) {
            for(JsonSelector selector : list) {
                // TODO: add combinedfieldselector

                Method method = klass.getMethod("set" + selector.getObjectField(), targetClass);
                method.invoke(currentObject, selector.getDataConverter(targetClass).read(object));
            }
            isInObject = true;
            objectIndex += 1;
            if(objectIndex.equals(objectLength))
                objectIndex = 0;

        }
        return currentObject;
    }

}

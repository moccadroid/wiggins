package org.lalelu.wiggins.requests.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.lalelu.wiggins.selectors.json.JsonSelector;

public class JsonObjectModel {
    private Class<?> klass = null;
    private ArrayList<JsonSelector> selectors = new ArrayList<JsonSelector>();

    private Integer objectIndex = 0;
    private Integer objectLength = 0;
    private boolean isInObject = false;
    private Object currentObject = null;

    private String field = "";
    private JsonObjectModel parent = null;

    public JsonObjectModel(Class<?> klass) {
        this.klass = klass;
    }

    public void addSelector(JsonSelector selector) {
        selectors.add(selector);
        objectLength += 1;
    }

    public List<JsonSelector> getSelectors() {
        return selectors;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public void createObject() throws IllegalAccessException, InstantiationException {
        currentObject = klass.newInstance();
        isInObject = false;
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

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return this.field;
    }

    public Object assembleObject(Class<?> targetClass, String targetField, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = klass.getMethod("set" + targetField, targetClass);
        method.invoke(currentObject, object);

        isInObject = true;
        objectIndex += 1;
        if(objectIndex.equals(objectLength))
            objectIndex = 0;

        return object;
    }

}

package org.lalelu.wiggins.requests.json;

import org.lalelu.wiggins.selectors.json.JsonSelector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonObjectModel {
    private Class klass = null;
    private ArrayList<JsonSelector> selectors = new ArrayList<JsonSelector>();

    private Integer objectIndex = 0;
    private Integer objectLength = 0;
    private boolean isInObject = false;
    private Object currentObject = null;

    private String field = "";

    public JsonObjectModel(Class klass) {
        this.klass = klass;
    }

    public void addSelector(JsonSelector selector) {
        selectors.add(selector);
        objectLength += 1;
    }

    public List<JsonSelector> getSelectors() {
        return selectors;
    }

    public Class getKlass() {
        return klass;
    }

    public void createObject() throws IllegalAccessException, InstantiationException {
        currentObject = klass.newInstance();
        isInObject = false;
    }

    public Object getCurrentObject() {
        return currentObject;
    }

    public Integer getObjectIndex() {
        return objectIndex;
    }

    public void setInObject(boolean isInObject) {
        this.isInObject = isInObject;
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

    public void check() throws IllegalAccessException, InstantiationException {
        if(currentObject == null)
            currentObject = klass.newInstance();
    }

    public Object assembleObject(Class targetClass, String targetField, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = klass.getMethod("set" + targetField, targetClass);
        method.invoke(currentObject, object);

        isInObject = true;
        objectIndex += 1;
        if(objectIndex.equals(objectLength))
            objectIndex = 0;

        return object;
    }

}

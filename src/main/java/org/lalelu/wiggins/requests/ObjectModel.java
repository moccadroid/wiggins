package org.lalelu.wiggins.requests;

import org.lalelu.wiggins.conditions.BreakCondition;
import org.lalelu.wiggins.conditions.Condition;
import org.lalelu.wiggins.selectors.DefaultValueSelector;
import org.lalelu.wiggins.selectors.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ObjectModel {
    protected Class<?> klass = null;
    protected List<Selector> selectorList = new ArrayList<Selector>();

    protected Map<String, List<Selector>> selectorMap = new HashMap<String, List<Selector>>();
    protected List<Selector> defaultValueSelectors = new ArrayList<Selector>();

    protected Map<String, Boolean> visitedMap = new HashMap<String, Boolean>();

    protected Object currentObject = null;
    protected boolean isInObject = false;
    protected boolean hasSelectors = false;
    protected Integer childrenCount = 0;
    protected Integer childrenIndex = 0;
    protected Integer objectLength = 0;
    protected Integer objectIndex = 0;

    protected ObjectModel parent = null;
    protected String parentField = "";

    protected Map<String, List<BreakCondition>> breakConditions = new HashMap<String, List<BreakCondition>>();
    protected boolean hasBreakConditions = false;
    protected boolean breakConditionFound = false;

    public ObjectModel(Class<?> klass) {
        this.klass = klass;
    }

    public Class<?> getKlass() {
        return this.klass;
    }

    public void addSelector(Selector selector) {
        if(selector instanceof DefaultValueSelector) {
            defaultValueSelectors.add(selector);
        } else {
            if (selectorMap.containsKey(selector.getSelectorPath())) {
                selectorMap.get(selector.getSelectorPath()).add(selector);
            } else {
                objectLength += 1;
                List<Selector> list = new ArrayList<Selector>();
                list.add(selector);
                selectorMap.put(selector.getSelectorPath(), list);
            }
        }
        hasSelectors = true;
        selectorList.add(selector);
    }

    public void createObject() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        currentObject = klass.newInstance();
        isInObject = false;
        childrenIndex = 0;
        visitedMap.clear();
        breakConditionFound = false;

        for(Selector selector : defaultValueSelectors) {
            Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
            method.invoke(currentObject, selector.getDataConverter().read(null));
        }
    }

    public void addBreakCondition(BreakCondition breakCondition) {
        if(breakConditions.containsKey(breakCondition.getField())) {
            breakConditions.get(breakCondition.getField()).add(breakCondition);
        } else {
            List<BreakCondition> list = new ArrayList<BreakCondition>();
            list.add(breakCondition);
            breakConditions.put(breakCondition.getField(), list);
        }
        hasBreakConditions = true;
    }

    public boolean breakConditionFound() {
        return breakConditionFound;
    }

    public void setBreakConditionFound(boolean breakConditionFound) {
        this.breakConditionFound = breakConditionFound;
    }

    public Map<String, List<BreakCondition>> getBreakConditions() {
        return breakConditions;
    }

    public boolean hasBreakConditions() {
        return hasBreakConditions;
    }

    public Integer getObjectIndex() {
        return this.objectIndex;
    }

    public boolean isInObject() {
        return this.isInObject;
    }

    public Object getCurrentObject() {
        return this.currentObject;
    }

    public boolean hasSelectors() {
        return hasSelectors;
    }

    public void increaseChildrenCount() {
        childrenCount += 1;
    }

    public boolean isComplete() {
        boolean allChildren = true;
        if(childrenCount > 0)
            allChildren = (childrenIndex.equals(childrenCount));
        return allChildren;
    }

    public void resetChildrenIndex() {
        childrenIndex = 0;
    }

    public void increaseChildrenIndex() {
        this.childrenIndex += 1;
    }

    public ObjectModel getParent() {
        return this.parent;
    }

    public void setParent(String parentField, ObjectModel parent) {
        this.parent = parent;
        this.parentField = parentField;
        this.parent.increaseChildrenCount();
    }

    public String getParentField() {
        return this.parentField;
    }

    public List<Selector> getSelectors() {
        return selectorList;
    }

}

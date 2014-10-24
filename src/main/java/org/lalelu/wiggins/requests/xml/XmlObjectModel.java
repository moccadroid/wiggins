package org.lalelu.wiggins.requests.xml;

import org.lalelu.wiggins.selectors.xml.XmlDefaultValueSelector;
import org.lalelu.wiggins.selectors.xml.XmlSelector;
import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlObjectModel {
    private Class<?> klass = null;
    private List<XmlSelector> selectorList = new ArrayList<XmlSelector>();

    private Map<String, List<XmlSelector>> selectorMap = new HashMap<String, List<XmlSelector>>();
    private List<XmlSelector> defaultValueSelectors = new ArrayList<XmlSelector>();


    private Object currentObject = null;
    private boolean isInObject = false;
    private boolean hasSelectors = false;
    private Integer childrenCount = 0;
    private Integer childrenIndex = 0;
    private Integer objectLength = 0;
    private Integer objectIndex = 0;

    private XmlObjectModel parent = null;
    private String parentField = "";

    public XmlObjectModel(Class<?> klass) {
        this.klass = klass;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public void addSelector(XmlSelector selector) {
        if(selector instanceof XmlDefaultValueSelector) {
            defaultValueSelectors.add(selector);
        } else {
            if (selectorMap.containsKey(selector.getSelectorPath())) {
                selectorMap.get(selector.getSelectorPath()).add(selector);
            } else {
                objectLength += 1;
                List<XmlSelector> list = new ArrayList<XmlSelector>();
                list.add(selector);
                selectorMap.put(selector.getSelectorPath(), list);
            }
        }
        hasSelectors = true;
        selectorList.add(selector);
    }

    public void createObject() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        currentObject = klass.newInstance();
        isInObject = false;
        childrenIndex = 0;

        for(XmlSelector selector : defaultValueSelectors) {
            Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
            method.invoke(currentObject, selector.getDataConverter().read(null));
        }
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
        return (childrenIndex.equals(childrenCount));
    }

    public void resetChildrenIndex() {
        childrenIndex = 0;
    }

    public void increaseChildrenIndex() {
        this.childrenIndex += 1;
    }

    public XmlObjectModel getParent() {
        return this.parent;
    }

    public void setParent(String parentField, XmlObjectModel parent) {
        this.parent = parent;
        this.parentField = parentField;
        this.parent.increaseChildrenCount();
    }

    public String getParentField() {
        return this.parentField;
    }

    public List<XmlSelector> getSelectors() {
        return selectorList;
    }

    public void assembleObject(String path, Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println(path + " " + element.getTagName());
        List<XmlSelector> list = selectorMap.get(path);
        if(list != null) {
            for(XmlSelector selector : list) {
                if(selector.getAttribute() != null) {
                    Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
                    method.invoke(currentObject, selector.getDataConverter().read(element.getAttribute(selector.getAttribute())));
                } else {
                    Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
                    method.invoke(currentObject, selector.getDataConverter().read(element.getTextContent()));
                }
            }
            isInObject = true;
            objectIndex += 1;
            if(objectIndex.equals(objectLength))
                objectIndex = 0;
        }
    }
}

package org.lalelu.wiggins.requests.xml;

import org.lalelu.wiggins.requests.ObjectModel;
import org.lalelu.wiggins.selectors.Selector;
import org.lalelu.wiggins.selectors.xml.XmlCombinedFieldSelector;
import org.lalelu.wiggins.selectors.xml.XmlSelector;
import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class XmlObjectModel extends ObjectModel {

    public XmlObjectModel(Class<?> klass) {
        super(klass);
    }

    public void assembleObject(String path, Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(visitedMap.get(path) != null)
            return;

        List<Selector> list = selectorMap.get(path);
        if(list != null) {
            for(Selector selector : list) {
                XmlSelector xmlSelector = (XmlSelector) selector;
                if(selector instanceof XmlCombinedFieldSelector) {
                    Method getMethod = klass.getMethod("get" + selector.getObjectField());
                    Object currentValue = getMethod.invoke(currentObject);

                    if(xmlSelector.getAttribute() != null) {
                        Method method = klass.getMethod("set" + xmlSelector.getObjectField(), xmlSelector.getFieldType());
                        method.invoke(currentObject, xmlSelector.getDataConverter().readCombined(currentValue, element.getAttribute(xmlSelector.getAttribute())));
                    } else {
                        Method method = klass.getMethod("set" + xmlSelector.getObjectField(), xmlSelector.getFieldType());
                        method.invoke(currentObject, xmlSelector.getDataConverter().readCombined(currentValue, element.getTextContent()));
                    }
                } else {
                    if(xmlSelector.getAttribute() != null) {
                        Method method = klass.getMethod("set" + xmlSelector.getObjectField(), xmlSelector.getFieldType());
                        method.invoke(currentObject, xmlSelector.getDataConverter().read(element.getAttribute(xmlSelector.getAttribute())));
                    } else {
                        Method method = klass.getMethod("set" + xmlSelector.getObjectField(), xmlSelector.getFieldType());
                        method.invoke(currentObject, xmlSelector.getDataConverter().read(element.getTextContent()));
                    }
                }


            }
            isInObject = true;
            objectIndex += 1;
            if(objectIndex.equals(objectLength))
                objectIndex = 0;
        }
        visitedMap.put(path, true);
    }

    public void joinOn(String joinPath, String value) {

    }
}

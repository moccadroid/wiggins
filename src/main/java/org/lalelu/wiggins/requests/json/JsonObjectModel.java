package org.lalelu.wiggins.requests.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.lalelu.wiggins.requests.ObjectModel;
import org.lalelu.wiggins.selectors.Selector;
import org.lalelu.wiggins.selectors.dataconverter.DataConverter;
import org.lalelu.wiggins.selectors.dataconverter.provider.DataConverterProvider;
import org.lalelu.wiggins.selectors.json.JsonCombinedFieldSelector;
import org.lalelu.wiggins.selectors.json.JsonDefaultValueSelector;
import org.lalelu.wiggins.selectors.json.JsonFieldSelector;
import org.lalelu.wiggins.selectors.json.JsonSelector;

public class JsonObjectModel extends ObjectModel {

    public JsonObjectModel(Class<?> klass) {
        super(klass);
    }

    public void assembleObject(Class<?> targetClass, String path, Object object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(visitedMap.get(path) != null)
            return;

        List<Selector> list = selectorMap.get(path);

        if(list != null) {
            for(Selector selector : list) {
                JsonSelector jsonSelector = (JsonSelector) selector;
                if(jsonSelector instanceof JsonCombinedFieldSelector) {
                    Method getMethod = klass.getMethod("get" + selector.getObjectField());
                    Object currentValue = getMethod.invoke(currentObject);

                    Method method = klass.getMethod("set" + selector.getObjectField(), selector.getFieldType());
                    method.invoke(currentObject, selector.getDataConverter().readCombined(currentValue, object));
                } else {
                    Method method = klass.getMethod("set" + jsonSelector.getObjectField(), targetClass);
                    method.invoke(currentObject, jsonSelector.getDataConverter(targetClass).read(object));
                }
            }
            isInObject = true;
            objectIndex += 1;
            if(objectIndex.equals(objectLength))
                objectIndex = 0;

        }
        visitedMap.put(path, true);
    }

}

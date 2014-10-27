package org.lalelu.wiggins.requests.csv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lalelu.wiggins.requests.ObjectModel;
import org.lalelu.wiggins.selectors.Selector;
import org.lalelu.wiggins.selectors.csv.CsvCombinedFieldSelector;
import org.lalelu.wiggins.selectors.csv.CsvDefaultValueSelector;
import org.lalelu.wiggins.selectors.csv.CsvSelector;

public class CsvObjectModel extends ObjectModel {

    public CsvObjectModel(Class<?> klass) {
        super(klass);
    }

    public boolean containsObjectField(String csvField) {
        return selectorMap.containsKey(csvField);
    }

    public void assembleObject(String csvHeader, String csvField) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        /*
        if(visitedMap.get(csvHeader) != null)
            return;
        */
        List<Selector> list = selectorMap.get(csvHeader);

        if(list != null) {
            for(Selector selector : list) {
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
        //visitedMap.put(csvHeader, true);
    }
}

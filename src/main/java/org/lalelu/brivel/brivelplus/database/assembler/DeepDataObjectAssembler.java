package org.lalelu.brivel.brivelplus.database.assembler;

import org.lalelu.brivel.brivelplus.requests.Request;
import org.lalelu.brivel.brivelplus.selectors.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeepDataObjectAssembler implements DataObjectAssembler {
    private List<Selector<?>> selectorList = null;
    private Object[] values = null;

    public <E> E assembleObject(Request<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        Class<E> klass = request.getKlass();
        E object = klass.newInstance();

        int count;
        for(count = 0; count < selectorList.size(); count++) {
            Selector<?> selector = selectorList.get(count);
            Method method = klass.getMethod("set" + selector.getFieldName(), selector.getType());
            method.invoke(object, selector.getDataConverter().read(values[count]));
        }

        Map<String, Request<?>> subRequests = request.getSubRequests();
        DeepDataObjectAssembler deepDataSubObjectAssembler = new DeepDataObjectAssembler();

        for(Map.Entry<String, Request<?>> entry : subRequests.entrySet()) {
            Request<?> subRequest = entry.getValue();
            deepDataSubObjectAssembler.setSelectorList(subRequest.getSelectSelectors());
            deepDataSubObjectAssembler.setValues( Arrays.copyOfRange(values, count, count + deepDataSubObjectAssembler.selectorList.size()));
            count += deepDataSubObjectAssembler.selectorList.size();

            Object subObject = subRequest.assembleAndAddObject(deepDataSubObjectAssembler);

            Method method = klass.getMethod("set" + entry.getKey(), subRequest.getKlass());
            method.invoke(object, subObject);
        }
        return object;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public void setSelectorList(List<Selector<?>> selectorList) {
        this.selectorList = selectorList;
    }
}

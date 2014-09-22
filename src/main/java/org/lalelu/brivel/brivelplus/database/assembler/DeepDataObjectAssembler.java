package org.lalelu.brivel.brivelplus.database.assembler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.lalelu.brivel.brivelplus.requests.Request;
import org.lalelu.brivel.brivelplus.selectors.Selector;

public class DeepDataObjectAssembler extends DefaultDataObjectAssembler {
    Object currentObject = null;
    String currentKeyValue = "";

    @Override
    public <E> E assembleObject(Request<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        Class<E> klass = request.getKlass();
        E object = klass.newInstance();

        int count;
        for(count = 0; count < selectorList.size(); count++) {
            Selector<?> selector = selectorList.get(count);

            if(selector.isKey()) {
                if(!currentKeyValue.equals(values[count]+"")) {
                    currentObject = object;
                } else {
                    if(currentObject != null)
                        object = (E) currentObject;
                }
                currentKeyValue = "" + values[count];
            }

            Method method = klass.getMethod(selector.getFieldName(), selector.getType());
            method.invoke(object, selector.getDataConverter().read(values[count]));
        }

        Map<String, Request<?>> subRequests = request.getSubRequests();
        DeepDataObjectAssembler deepDataSubObjectAssembler = new DeepDataObjectAssembler();

        for(Map.Entry<String, Request<?>> entry : subRequests.entrySet()) {
            Request<?> subRequest = entry.getValue();
            int numValuesProcessed = countValuesRequired(subRequest);
            
            deepDataSubObjectAssembler.setSelectorList(subRequest.getSelectSelectors());
            deepDataSubObjectAssembler.setValues(Arrays.copyOfRange(values, count, count + numValuesProcessed));

            Object subObject = subRequest.assembleAndAddObject(deepDataSubObjectAssembler);
            count += numValuesProcessed;
            
            Method method = klass.getMethod(entry.getKey(), subRequest.getKlass());
            method.invoke(object, subObject);
        }
        return object;
    }
    
    private int countValuesRequired(Request<?> request) {
    	int count = request.getSelectors().size();
    	for(Request<?> subRequest : request.getSubRequests().values())
    		count += countValuesRequired(subRequest);
    	return count;
    }
}

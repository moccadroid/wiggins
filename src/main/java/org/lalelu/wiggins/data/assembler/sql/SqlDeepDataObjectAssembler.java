package org.lalelu.wiggins.data.assembler.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.lalelu.wiggins.requests.sql.SqlRequest;
import org.lalelu.wiggins.selectors.sql.SqlSelector;

public class SqlDeepDataObjectAssembler extends SqlDefaultDataObjectAssembler {
    Object currentObject = null;
    String currentKeyValue = "";

    @Override
    public <E> E assembleObject(SqlRequest<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        Class<E> klass = request.getKlass();
        E object = klass.newInstance();

        int count;
        for(count = 0; count < selectorList.size(); count++) {
            SqlSelector<?> selector = selectorList.get(count);

            if(selector.isKey()) {
                if(!currentKeyValue.equals(values[count]+"")) {
                    currentObject = object;
                } else {
                    if(currentObject != null)
                        object = request.getKlass().cast(currentObject);
                }
                currentKeyValue = "" + values[count];
            }

            Method method = klass.getMethod(selector.getFieldName(), selector.getType());
            method.invoke(object, selector.getDataConverter().read(values[count]));
        }

        Map<String, SqlRequest<?>> subRequests = request.getSubRequests();
        SqlDeepDataObjectAssembler deepDataSubObjectAssembler = new SqlDeepDataObjectAssembler();

        for(Map.Entry<String, SqlRequest<?>> entry : subRequests.entrySet()) {
            SqlRequest<?> subRequest = entry.getValue();
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
    
    private int countValuesRequired(SqlRequest<?> request) {
    	int count = request.getSelectors().size();
    	for(SqlRequest<?> subRequest : request.getSubRequests().values())
    		count += countValuesRequired(subRequest);
    	return count;
    }
}

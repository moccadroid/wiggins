package org.lalelu.brivel.brivelplus.database.assembler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lalelu.brivel.brivelplus.requests.Request;
import org.lalelu.brivel.brivelplus.selectors.Selector;

public class ShallowDataObjectAssembler extends DefaultDataObjectAssembler {
    @Override
    public <E> E assembleObject(Request<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        Class<E> klass = request.getKlass();
        E object = klass.newInstance();

        int count;
        for(count = 0; count < selectorList.size(); count++) {
            Selector<?> selector = selectorList.get(count);
            Method method = klass.getMethod("set" + selector.getFieldName(), selector.getType());
            method.invoke(object, selector.getDataConverter().read(values[count]));
        }

        return object;
    }
}

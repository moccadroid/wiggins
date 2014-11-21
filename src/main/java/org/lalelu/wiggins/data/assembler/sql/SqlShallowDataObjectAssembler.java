package org.lalelu.wiggins.data.assembler.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lalelu.wiggins.requests.sql.SqlRequest;
import org.lalelu.wiggins.selectors.sql.SqlSelector;

public class SqlShallowDataObjectAssembler extends SqlDefaultDataObjectAssembler {
    @Override
    public <E> E assembleObject(SqlRequest<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        Class<E> klass = request.getKlass();
        E object = klass.newInstance();

        int count;
        for(count = 0; count < selectorList.size(); count++) {
            SqlSelector<?> selector = selectorList.get(count);
            Method method = klass.getMethod("set" + selector.getFieldName(), selector.getType());
            method.invoke(object, selector.getDataConverter().read(values[count]));
        }

        return object;
    }
}

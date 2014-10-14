package org.lalelu.wiggins.data.assembler.json;

import org.lalelu.wiggins.data.assembler.DataObjectAssembler;
import org.lalelu.wiggins.requests.sql.Request;

import java.lang.reflect.InvocationTargetException;

public class JsonDefaultDataObjectAssembler implements DataObjectAssembler {
    @Override
    public <E> E assembleObject(Request<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        return null;
    }
}

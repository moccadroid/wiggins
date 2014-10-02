package org.lalelu.wiggins.database.assembler;

import org.lalelu.wiggins.requests.Request;

import java.lang.reflect.InvocationTargetException;

public interface DataObjectAssembler {
    public <E> E assembleObject(Request<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException;
}

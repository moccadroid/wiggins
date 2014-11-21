package org.lalelu.wiggins.data.assembler;

import org.lalelu.wiggins.requests.sql.SqlRequest;

import java.lang.reflect.InvocationTargetException;

public interface DataObjectAssembler {
    public <E> E assembleObject(SqlRequest<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException;
}

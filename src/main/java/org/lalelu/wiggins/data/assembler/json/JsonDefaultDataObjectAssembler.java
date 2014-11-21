package org.lalelu.wiggins.data.assembler.json;

import org.lalelu.wiggins.data.assembler.DataObjectAssembler;
import org.lalelu.wiggins.requests.sql.SqlRequest;

import java.lang.reflect.InvocationTargetException;

public class JsonDefaultDataObjectAssembler implements DataObjectAssembler {
    @Override
    public <E> E assembleObject(SqlRequest<E> request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        return null;
    }
}

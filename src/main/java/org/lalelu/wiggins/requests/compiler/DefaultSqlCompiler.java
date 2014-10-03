package org.lalelu.wiggins.requests.compiler;

import org.lalelu.wiggins.requests.sql.RequestData;

public class DefaultSqlCompiler<T> implements SqlCompiler<T> {
    @Override
    public String compile(RequestData<T> requestData) {
        return null;
    }
}

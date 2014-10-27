package org.lalelu.wiggins.requests.sql.compiler;

import org.lalelu.wiggins.requests.sql.RequestData;

public interface SqlCompiler<T> {
    public String compile(RequestData<T> requestData);
}

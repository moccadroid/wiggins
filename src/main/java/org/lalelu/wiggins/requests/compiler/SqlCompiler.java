package org.lalelu.wiggins.requests.compiler;

import org.lalelu.wiggins.requests.sql.RequestData;

public interface SqlCompiler<T> {
    public String compile(RequestData<T> requestData);
}

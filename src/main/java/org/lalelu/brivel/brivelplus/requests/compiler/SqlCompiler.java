package org.lalelu.brivel.brivelplus.requests.compiler;

import org.lalelu.brivel.brivelplus.requests.RequestData;

public interface SqlCompiler<T> {
    public String compile(RequestData<T> requestData);
}

package org.lalelu.wiggins.requests.sql.compiler;

import org.lalelu.wiggins.requests.sql.SqlRequestData;

public interface SqlCompiler<T> {
    public String compile(SqlRequestData<T> requestData);
}

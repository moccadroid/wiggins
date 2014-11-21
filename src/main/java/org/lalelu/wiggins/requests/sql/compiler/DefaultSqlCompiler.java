package org.lalelu.wiggins.requests.sql.compiler;

import org.lalelu.wiggins.requests.sql.SqlRequestData;

public class DefaultSqlCompiler<T> implements SqlCompiler<T> {
    @Override
    public String compile(SqlRequestData<T> requestData) {
        return null;
    }
}

package org.lalelu.wiggins.data;

import java.util.List;

import org.lalelu.wiggins.WigginsCentral;
import org.lalelu.wiggins.data.assembler.sql.SqlDeepDataObjectAssembler;
import org.lalelu.wiggins.data.provider.DatabaseAccessProvider;
import org.lalelu.wiggins.errors.ExceptionPool;
import org.lalelu.wiggins.requests.sql.SqlRequest;

public class DatabaseFacade {
    private DatabaseAccessProvider databaseAccessProvider = null;

    public DatabaseFacade() {
        this.databaseAccessProvider = WigginsCentral.getDatabaseAccessProvider();
    }

    public <T> SqlRequest<T> updateRequest(SqlRequest<T> request) {
        if(!request.isUpdateCompiled()) {
            request.compileUpdate();
        }

        databaseAccessProvider.execute(request.getUpdateSql());

        return request;
    }

    public <T> SqlRequest<T> mergeRequest(SqlRequest<T> request) {
        if(!request.isInsertCompiled()) {
            request.compileInsert();
        }

        databaseAccessProvider.execute(request.getInsertSql());

        return request;
    }

    public <T> SqlRequest<T> getResult(SqlRequest<T> request) {
        try {
            if (!request.isSelectCompiled())
                request.compileSelect();

            String sql = request.getSelectSql();
            List<Object[]> result = databaseAccessProvider.getResultList(sql);

            SqlDeepDataObjectAssembler assembler = new SqlDeepDataObjectAssembler();
            for (Object[] row : result) {
                assembler.setSelectorList(request.getSelectSelectors());
            	assembler.setValues(row);
            	request.assembleAndAddObject(assembler);
            }

        } catch (Exception e) {
            ExceptionPool.getInstance().addException(e);
        }

        return request;
    }
}

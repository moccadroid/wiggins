package org.lalelu.wiggins.database;

import java.util.List;

import org.lalelu.wiggins.BrivelCentral;
import org.lalelu.wiggins.database.assembler.DeepDataObjectAssembler;
import org.lalelu.wiggins.database.provider.DatabaseAccessProvider;
import org.lalelu.wiggins.errors.ErrorHandler;
import org.lalelu.wiggins.requests.Request;

public class DatabaseFacade {
    private DatabaseAccessProvider databaseAccessProvider = null;

    public DatabaseFacade() {
        this.databaseAccessProvider = BrivelCentral.getDatabaseAccessProvider();
    }

    public <T> Request<T> updateRequest(Request<T> request) {
        if(!request.isUpdateCompiled()) {
            request.compileUpdate();
        }

        databaseAccessProvider.execute(request.getUpdateSql());

        return request;
    }

    public <T> Request<T> mergeRequest(Request<T> request) {
        if(!request.isInsertCompiled()) {
            request.compileInsert();
        }

        databaseAccessProvider.execute(request.getInsertSql());

        return request;
    }

    public <T> Request<T> getResult(Request<T> request) {
        try {
            if (!request.isSelectCompiled())
                request.compileSelect();

            String sql = request.getSelectSql();
            List<Object[]> result = databaseAccessProvider.getResultList(sql);

            DeepDataObjectAssembler assembler = new DeepDataObjectAssembler();
            for (Object[] row : result) {
            	assembler.setSelectorList(request.getSelectSelectors());
            	assembler.setValues(row);
            	request.assembleAndAddObject(assembler);
            }

        } catch (Exception e) {
            ErrorHandler.resolveException(e, true);
        }

        return request;
    }
}

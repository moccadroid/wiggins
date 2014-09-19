package org.lalelu.brivel.brivelplus.database;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.lalelu.brivel.brivelplus.BrivelCentral;
import org.lalelu.brivel.brivelplus.database.assembler.DeepDataObjectAssembler;
import org.lalelu.brivel.brivelplus.database.provider.DatabaseAccessProvider;
import org.lalelu.brivel.brivelplus.requests.Request;

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

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }
}

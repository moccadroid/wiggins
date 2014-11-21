package org.lalelu.wiggins.requests.sql.compiler;

import org.lalelu.wiggins.requests.sql.SqlRequestData;
import org.lalelu.wiggins.selectors.sql.SqlSelector;

public class SelectSqlCompiler<T> extends DefaultSqlCompiler<T> {

    public String compile(SqlRequestData<T> data) {

        String select = "SELECT ";
        for(Object object : data.getCompSelectList()) {
            SqlSelector<?> selector = (SqlSelector<?>) object;
            if(selector.selectField() == null || selector.selectField().isEmpty() || selector.tableField() == null || selector.tableField().isEmpty())
                continue;

            select += selector.tableField() + "." + selector.selectField() + " AS "+ selector.getAlias() +" ,";
        }
        select = select.substring(0, select.length() -1);


        String from = " FROM ";
        if(!data.getCompFromList().isEmpty()) {
            SqlSelector<?> selector = (SqlSelector<?>) data.getCompFromList().get(0);

            if (!from.contains(selector.tableField()))
                from += selector.tableField() + ",";

            from = from.substring(0, from.length() - 1);
        }

        String join = "";
        for(Object object : data.getCompJoinList()) {
            SqlSelector<?> selector = (SqlSelector<?>) object;
            if(selector.leftJoinSide() == null || selector.leftJoinSide().isEmpty() || selector.rightJoinSide() == null || selector.rightJoinSide().isEmpty())
                continue;

            String tmpJoin = " JOIN ";
            tmpJoin += selector.tableField() + " ON ";
            tmpJoin += selector.leftJoinSide() + " = " + selector.rightJoinSide();

            if(!join.contains(tmpJoin))
                join += tmpJoin;
        }

        String where = " WHERE ";
        for(Object object : data.getCompWhereList()) {
            SqlSelector<?> selector = (SqlSelector<?>) object;
            if(selector.whereField() == null || selector.whereField().isEmpty())
                continue;

            if(!where.endsWith(" WHERE "))
                where += " AND ";
            where += selector.whereField() + ",";
        }
        where = (where.equals(" WHERE ")) ? "" : where.substring(0, where.length() -1);

        String compiledSelectQuery = "";
        compiledSelectQuery += select;
        compiledSelectQuery += from;
        compiledSelectQuery += join;
        compiledSelectQuery += where;
        compiledSelectQuery += (data.getOrderBySelector().orderField() == null) ? "" : data.getOrderBySelector().orderField();
        compiledSelectQuery += (data.getLimitSelector().limitField() == null) ? "" : data.getLimitSelector().limitField();

        data.setCompiledSelectQuery(compiledSelectQuery);
        data.setSelectCompiled(true);

        return compiledSelectQuery;
    }
}

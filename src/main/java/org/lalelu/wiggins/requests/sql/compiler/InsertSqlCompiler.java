package org.lalelu.wiggins.requests.sql.compiler;

import org.lalelu.wiggins.requests.sql.RequestData;
import org.lalelu.wiggins.selectors.sql.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InsertSqlCompiler<T> extends DefaultSqlCompiler<T> {

    public String compile(RequestData<T> data) {
        String insert = "INSERT INTO ";
        if(!data.getCompFromList().isEmpty()) {
            Selector<?> selector = (Selector<?>) data.getCompFromList().get(0);

            insert += selector.tableField() + " ";
        }

        String fields = "(";
        for(Object object : data.getCompSelectList()) {
            Selector<?> selector = (Selector<?>) object;
            if(selector.selectField().equals("id"))
                continue;
            fields += selector.selectField() + ",";
        }
        fields = fields.substring(0, fields.length() - 1);
        fields += ")";

        String values = " VALUES ";
        for(Object object : data.getResultList()) {
            values += "(";
            for (Object selectorObject : data.getCompSelectList()) {
                Selector<?> selector = (Selector<?>) selectorObject;
                if(selector.selectField().equals("id"))
                    continue;

                try {
                    Method method = data.getKlass().getMethod("get"+selector.getFieldName());
                    method.setAccessible(true);
                    values += selector.getDataConverter().write(method.invoke(object)) + ",";
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            values = values.substring(0, values.length() - 1);
            values += "),";
        }
        values = values.substring(0, values.length() -1);

        String compiledInsertQuery = "";
        compiledInsertQuery += insert;
        compiledInsertQuery += fields;
        compiledInsertQuery += values;
        compiledInsertQuery += "; ";

        data.setCompiledInsertQuery(compiledInsertQuery);
        data.setInsertCompiled(true);

        return compiledInsertQuery;
    }
}

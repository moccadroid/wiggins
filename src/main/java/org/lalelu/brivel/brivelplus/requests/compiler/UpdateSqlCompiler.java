package org.lalelu.brivel.brivelplus.requests.compiler;

import org.lalelu.brivel.brivelplus.requests.Request;
import org.lalelu.brivel.brivelplus.requests.RequestData;
import org.lalelu.brivel.brivelplus.selectors.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class UpdateSqlCompiler<T> extends DefaultSqlCompiler<T> {

    public String compile(RequestData<T> data) {
        String compiledUpdateQuery = "";

        try {
            for(Object object : data.getResultList()) {
                // why? : T object = (T) objectObject;

                String tableName = "tableName";
                String update = "UPDATE ";
                if (!data.getFromSelectors().isEmpty()) {
                    Selector<?> selector = (Selector<?>) data.getFromSelectors().get(0);
                    update += selector.tableField() + " AS " + tableName + " ";
                }

                String values = " SET ";
                String idField = "";
                for (Object selectorObject : data.getSelectSelectors()) {
                    Selector<?> selector = (Selector<?>) selectorObject;
                    Method method = data.getKlass().getMethod("get" + selector.getFieldName());
                    method.setAccessible(true);

                    if (selector.selectField().equalsIgnoreCase("id"))
                        idField = "" + selector.getDataConverter().write(method.invoke(object));
                    else
                        values += selector.selectField() + " = " + selector.getDataConverter().write(method.invoke(object)) + ",";

                }
                values = values.substring(0, values.length() - 1);

                String where = " WHERE " + tableName + ".id = " + idField;

                compiledUpdateQuery += update;
                compiledUpdateQuery += values;
                compiledUpdateQuery += where;
                compiledUpdateQuery += "; ";

                for(Object mapEntry : data.getSubRequests().entrySet()) {
                    Map.Entry<?,?> entry = (Map.Entry<?,?>) mapEntry;
                    Request<?> subRequest = (Request<?>) entry.getValue();

                    Method method = data.getKlass().getMethod("get"+ subRequest.getName());
                    method.setAccessible(true);

                    Object subObject = method.invoke(object);

                    tableName = "tableName";
                    update = "UPDATE ";
                    if (!subRequest.getFromSelectors().isEmpty()) {
                        Selector<?> selector = (Selector<?>) subRequest.getFromSelectors().get(0);
                        update += selector.tableField() + " AS " + tableName + " ";
                    }

                    values = " SET ";
                    idField = "";
                    for (Object objSelector : subRequest.getSelectSelectors()) {
                        Selector<?> selector = (Selector<?>) objSelector;

                        method = subRequest.getKlass().getMethod("get" + selector.getFieldName());
                        method.setAccessible(true);

                        if (selector.selectField().equalsIgnoreCase("id"))
                            idField = "" + selector.getDataConverter().write(method.invoke(subObject));
                        else
                            values += selector.selectField() + " = " + selector.getDataConverter().write(method.invoke(subObject)) + ",";

                    }
                    values = values.substring(0, values.length() - 1);

                    where = " WHERE " + tableName + ".id = " + idField;

                    compiledUpdateQuery += update;
                    compiledUpdateQuery += values;
                    compiledUpdateQuery += where;
                    compiledUpdateQuery += "; ";
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        data.setCompiledUpdateQuery(compiledUpdateQuery);
        data.setUpdateCompiled(true);

        return compiledUpdateQuery;
    }
}

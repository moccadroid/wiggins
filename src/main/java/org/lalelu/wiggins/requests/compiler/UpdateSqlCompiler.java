package org.lalelu.wiggins.requests.compiler;

import org.lalelu.wiggins.requests.Request;
import org.lalelu.wiggins.requests.RequestData;
import org.lalelu.wiggins.selectors.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UpdateSqlCompiler<T> extends DefaultSqlCompiler<T> {

    public String compile(RequestData<T> data) {
        String compiledUpdateQuery = "";

        try {
            for(Object object : data.getResultList()) {
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

                    String methodName = selector.getFieldName();
                    Method method = data.getKlass().getMethod("get" + methodName.substring(3, methodName.length()));
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


                    String methodName = subRequest.getGetMethodName();
                    if(!subRequest.isMany()) {
                        methodName = "get" + subRequest.getName().substring(3, subRequest.getName().length());
                    }

                    Method method = data.getKlass().getMethod(methodName);
                    method.setAccessible(true);

                    Object subObject = method.invoke(object);

                    if(subRequest.isMany()) {
                        if(subObject instanceof Collection<?>) {
                            List<Object> objectList = (List<Object>) subObject;
                            System.out.println(objectList.size());
                            for(Object o : objectList) {
                                compiledUpdateQuery += createUpdate(subRequest, o) + "; ";
                            }
                        } else {
                            // some error handling I suppose...
                        }
                    } else {
                        compiledUpdateQuery += createUpdate(subRequest, subObject) + "; ";
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        data.setCompiledUpdateQuery(compiledUpdateQuery);
        data.setUpdateCompiled(true);

        return compiledUpdateQuery;
    }

    private String createUpdate(Request request, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String tableName = "tableName";
        String update = "UPDATE ";
        if (!request.getFromSelectors().isEmpty()) {
            Selector<?> selector = (Selector<?>) request.getFromSelectors().get(0);
            update += selector.tableField() + " AS " + tableName + " ";
        }

        String values = " SET ";
        String idField = "";
        for (Object objSelector : request.getSelectSelectors()) {
            Selector<?> selector = (Selector<?>) objSelector;

            String methodName = selector.getFieldName();
            Method method = request.getKlass().getMethod("get" + methodName.substring(3, methodName.length()));
            method.setAccessible(true);

            if (selector.selectField().equalsIgnoreCase("id"))
                idField = "" + selector.getDataConverter().write(method.invoke(object));
            else
                values += selector.selectField() + " = " + selector.getDataConverter().write(method.invoke(object)) + ",";
        }
        values = values.substring(0, values.length() - 1);

        String where = " WHERE " + tableName + ".id = " + idField;

        return update + values + where;
    }
}

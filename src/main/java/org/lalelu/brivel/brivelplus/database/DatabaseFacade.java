package org.lalelu.brivel.brivelplus.database;

import org.lalelu.brivel.brivelplus.BrivelCentral;
import org.lalelu.brivel.brivelplus.database.provider.DatabaseAccessProvider;
import org.lalelu.brivel.brivelplus.requests.Request;
import org.lalelu.brivel.brivelplus.selectors.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseFacade {
    private DatabaseAccessProvider databaseAccessProvider = null;

    public DatabaseFacade() {
        this.databaseAccessProvider = BrivelCentral.getDatabaseAccessProvider();
    }

    public Request updateRequest(Request request) {
        if(!request.isUpdateCompiled()) {
            request.compileUpdate();
        }

        return request;
    }

    public Request mergeRequest(Request request) {
        if(!request.isInsertCompiled()) {

        }

        return request;
    }

    public Request getResult(Request request) {
        try {
            if (!request.isSelectCompiled())
                request.compileSelect();

            String sql = request.getSelectSql();
            List<Object[]> result = databaseAccessProvider.getResultList(sql);

            for (Object[] row : result) {
                List<Selector<?>> selectSelectors = request.getSelectSelectors();
                Class<?> klass = request.getKlass();
                Object object = klass.newInstance();

                int selectorCounter = 0;
                for(int i = 0; i < selectSelectors.size(); i++, selectorCounter = i) {
                    Selector selector = selectSelectors.get(i);
                    if (object != null) {
                        Method method = klass.getMethod("set" + selector.getFieldName(), selector.getType());
                        method.invoke(object, selector.getDataConverter().read(row[i]));
                    }
                }

                List<Selector<?>> tmpSelectorList = new ArrayList<Selector<?>>();
                tmpSelectorList.addAll(selectSelectors);
                Map<String, Request> subRequests = request.getSubRequests();
                for(Map.Entry<String, Request> entry : subRequests.entrySet()) {
                    Request subRequest = entry.getValue();

                    if(!subRequest.isDataRequest())
                        continue;

                    Class subKlass = subRequest.getKlass();
                    Object subObject = subKlass.newInstance();
                    tmpSelectorList.addAll(subRequest.getSelectSelectors());

                    for(int i = selectorCounter; i < tmpSelectorList.size(); i++, selectorCounter++) {
                        Selector selector = tmpSelectorList.get(i);
                        if (subObject != null) {
                            Method method = subKlass.getMethod("set" + selector.getFieldName(), selector.getType());
                            method.invoke(subObject, selector.getDataConverter().read(row[i]));
                        }
                    }
                    Method method = klass.getMethod("set" + entry.getKey(), subRequest.getKlass());
                    method.invoke(object, subObject);
                    subRequest.addObject(subObject);
                }

                request.addObject(object);
            }

        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
        } catch (InstantiationException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return request;
    }
}

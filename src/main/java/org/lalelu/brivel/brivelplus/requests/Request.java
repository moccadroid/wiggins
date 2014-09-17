package org.lalelu.brivel.brivelplus.requests;

import org.lalelu.brivel.brivelplus.selectors.LimitSelector;
import org.lalelu.brivel.brivelplus.selectors.OrderBySelector;
import org.lalelu.brivel.brivelplus.selectors.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Request<T> {
    private List<Selector<?>> selectorList = new ArrayList<Selector<?>>();

    private List<T> resultList = new ArrayList<T>();
    private Class<T> klass = null;

    private Map<String, Request> subRequests = new HashMap<String, Request>();

    private List<Selector<?>> selectSelectors = new ArrayList<Selector<?>>();
    private List<Selector<?>> joinSelectors = new ArrayList<Selector<?>>();
    private List<Selector<?>> whereSelectors = new ArrayList<Selector<?>>();
    private List<Selector<?>> fromSelectors = new ArrayList<Selector<?>>();

    private List<Selector<?>> compSelectList = new ArrayList<Selector<?>>();
    private List<Selector<?>> compJoinList = new ArrayList<Selector<?>>();
    private List<Selector<?>> compWhereList = new ArrayList<Selector<?>>();
    private List<Selector<?>> compFromList = new ArrayList<Selector<?>>();
    private Selector<?> limitSelector = new LimitSelector("");
    private Selector<?> orderBySelector = new OrderBySelector("", "");

    private boolean isDataRequest = true;

    private String name = "";
    private boolean isSelectCompiled = false;
    private boolean isInsertCompiled = false;
    private boolean isUpdateCompiled = false;
    private String compiledSelectQuery = "";
    private String compiledInsertQuery = "";
    private String compiledUpdateQuery = "";

    public Request(Class<T> klass) {
        this.klass = klass;
    }

    public void addSelector(Selector<?> selector) {
        selectorList.add(selector);
        isSelectCompiled = false;
        isInsertCompiled = false;
        isUpdateCompiled = false;
    }

    public List<Selector<?>> getSelectors() {
        return selectorList;
    }

    public void compileUpdate() {
        createSelectorLists();

        try {
            for(T object : resultList) {

                String tableName = "tableName";
                String update = "UPDATE ";
                if (!fromSelectors.isEmpty()) {
                    Selector selector = fromSelectors.get(0);
                    update += selector.tableField() + " AS " + tableName + " ";
                }

                String values = " SET ";
                String idField = "";
                for (Selector selector : selectSelectors) {
                    Method method = klass.getMethod("get" + selector.getFieldName());
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

                for(Map.Entry<String, Request> entry : getSubRequests().entrySet()) {
                    Request subRequest = entry.getValue();

                    Method method = klass.getMethod("get"+ subRequest.getName());
                    method.setAccessible(true);

                    Object subObject = method.invoke(object);

                    tableName = "tableName";
                    update = "UPDATE ";
                    if (!subRequest.getFromSelectors().isEmpty()) {
                        Selector selector = (Selector) subRequest.getFromSelectors().get(0);
                        update += selector.tableField() + " AS " + tableName + " ";
                    }

                    values = " SET ";
                    idField = "";
                    for (Object objSelector : subRequest.getSelectSelectors()) {
                        Selector selector = (Selector) objSelector;

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

        isUpdateCompiled = true;
    }

    public void compileInsert() {
        createSelectorLists();

        String insert = "INSERT INTO ";
        if(!compFromList.isEmpty()) {
            Selector selector = compFromList.get(0);

            insert += selector.tableField() + " ";
        }

        String fields = "(";
        for(Selector selector : compSelectList) {
            if(selector.selectField().equals("id"))
                continue;
            fields += selector.selectField() + ",";
        }
        fields = fields.substring(0, fields.length() - 1);
        fields += ")";

        String values = " VALUES ";
        for(T object : resultList) {
            values += "(";
            for (Selector selector : compSelectList) {
                if(selector.selectField().equals("id"))
                    continue;

                try {
                    Method method = klass.getMethod("get"+selector.getFieldName());
                    method.setAccessible(true);
                    values += selector.getDataConverter().write(method.invoke(object)) + ",";
                    System.out.println(method.invoke(object));
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

        compiledInsertQuery += insert;
        compiledInsertQuery += fields;
        compiledInsertQuery += values;

        isUpdateCompiled = true;
    }

    public void compileSelect() {
        createSelectorLists();

        String select = "SELECT ";
        for(Selector selector : compSelectList) {
            if(selector.selectField() == null || selector.selectField().isEmpty() || selector.tableField() == null || selector.tableField().isEmpty())
                continue;

            select += selector.tableField() + "." + selector.selectField() + " AS "+ selector.getAlias() +" ,";
        }
        select = select.substring(0, select.length() -1);


        String from = " FROM ";
        if(!compFromList.isEmpty()) {
            Selector selector = compFromList.get(0);

            if (!from.contains(selector.tableField()))
                from += selector.tableField() + ",";

            from = from.substring(0, from.length() - 1);
        }

        String join = "";
        for(Selector selector : compJoinList) {
            if(selector.joinField() == null || selector.joinField().isEmpty())
                continue;

            String tmpJoin = " JOIN ";
            tmpJoin += selector.tableField() + " ON ";
            tmpJoin += selector.joinField() + " = " + selector.tableField()+".id";

            if(!join.contains(tmpJoin))
                join += tmpJoin;
        }

        String where = " WHERE ";
        for(Selector selector : compWhereList) {
            if(selector.whereField() == null || selector.whereField().isEmpty())
                continue;

            if(!where.endsWith(" WHERE "))
                where += " AND ";
            where += selector.whereField() + ",";
        }
        where = (where.equals(" WHERE ")) ? "" : where.substring(0, where.length() -1);

        compiledSelectQuery += select;
        compiledSelectQuery += from;
        compiledSelectQuery += join;
        compiledSelectQuery += where;
        compiledSelectQuery += (orderBySelector.orderField() == null) ? "" : orderBySelector.orderField();
        compiledSelectQuery += (limitSelector.limitField() == null) ? "" : limitSelector.limitField();

        isSelectCompiled = true;
    }

    public boolean isSelectCompiled() {
        return isSelectCompiled;
    }

    public boolean isInsertCompiled() {
        return isInsertCompiled;
    }

    public boolean isUpdateCompiled() {
        return isUpdateCompiled;
    }

    public void addSubRequest(String name, Request request) {
        subRequests.put(name, request);
        request.setName(name);
        isSelectCompiled = false;
        isInsertCompiled = false;
        isUpdateCompiled = false;
    }

    public Map<String, Request> getSubRequests() {
        return subRequests;
    }

    public String getSelectSql() {
        return compiledSelectQuery;
    }

    public String getUpdateSql() {
        return compiledUpdateQuery;
    }
    public String getInsertSql() {
        return compiledInsertQuery;
    }

    public List<Selector<?>> getSelectSelectors() {
        return selectSelectors;
    }

    public List<Selector<?>> getJoinSelectors() {
        return joinSelectors;
    }

    public List<Selector<?>> getWhereSelectors() {
        return whereSelectors;
    }

    public List<Selector<?>> getFromSelectors() {
        return fromSelectors;
    }

    public List<T> getResultList() {
        return Collections.unmodifiableList(resultList);
    }

    public void addObject(T object) {
        this.resultList.add(object);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDataRequest(boolean isDataRequest) {
        this.isDataRequest = isDataRequest;
    }

    public boolean isDataRequest() {
        return isDataRequest;
    }

    public Class<T> getKlass() {
        return klass;
    }

    private void createSelectorLists() {
        compSelectList.clear();
        compJoinList.clear();
        compWhereList.clear();
        compFromList.clear();
        selectSelectors.clear();
        joinSelectors.clear();
        whereSelectors.clear();
        fromSelectors.clear();

        for(Selector<?> selector : selectorList) {
            if(selector.selectField() != null)
                selectSelectors.add(selector);
            if(selector.joinField() != null)
                joinSelectors.add(selector);
            if(selector.whereField() != null)
                whereSelectors.add(selector);
            if(selector.tableField() != null)
                fromSelectors.add(selector);
            if(selector.limitField() != null)
                limitSelector = selector;
            if(selector.orderField() != null)
                orderBySelector = selector;
        }
        compSelectList.addAll(selectSelectors);
        compJoinList.addAll(joinSelectors);
        compWhereList.addAll(whereSelectors);
        compFromList.addAll(fromSelectors);

        for(Map.Entry<String, Request> entry : subRequests.entrySet()) {
            if(!entry.getValue().isSelectCompiled())
                entry.getValue().compileSelect();

            compSelectList.addAll(entry.getValue().getSelectSelectors());
            compJoinList.addAll(entry.getValue().getJoinSelectors());
            compWhereList.addAll(entry.getValue().getWhereSelectors());
            compFromList.addAll(entry.getValue().getFromSelectors());
        }

    }
}

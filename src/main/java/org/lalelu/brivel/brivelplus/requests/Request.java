package org.lalelu.brivel.brivelplus.requests;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.lalelu.brivel.brivelplus.requests.compiler.InsertSqlCompiler;
import org.lalelu.brivel.brivelplus.requests.compiler.SelectSqlCompiler;
import org.lalelu.brivel.brivelplus.requests.compiler.SqlCompiler;
import org.lalelu.brivel.brivelplus.requests.compiler.UpdateSqlCompiler;
import org.lalelu.brivel.brivelplus.selectors.Selector;

public class Request<T> {

    RequestData<T> requestData = new RequestData<T>();

    private String name = "";

    private SqlCompiler<T> selectSqlCompiler = null;
    private SqlCompiler<T> insertSqlCompiler = null;
    private SqlCompiler<T> updateSqlCompiler = null;

    public Request(Class<T> klass) {
        requestData.setKlass(klass);
        selectSqlCompiler = new SelectSqlCompiler<T>();
        insertSqlCompiler = new InsertSqlCompiler<T>();
        updateSqlCompiler = new UpdateSqlCompiler<T>();
    }

    public void addSelector(Selector<?> selector) {
        requestData.getSelectorList().add(selector);
        requestData.setSelectCompiled(false);
        requestData.setInsertCompiled(false);
        requestData.setUpdateCompiled(false);
    }

    public List<Selector<?>> getSelectors() {
        return requestData.getSelectorList();
    }

    public void compileUpdate() {
        createSelectorLists();

        updateSqlCompiler.compile(requestData);
    }

    public void compileInsert() {
        createSelectorLists();

        insertSqlCompiler.compile(requestData);
    }

    public void compileSelect() {
        createSelectorLists();

        selectSqlCompiler.compile(requestData);
    }

    public boolean isSelectCompiled() {
        return requestData.isSelectCompiled();
    }

    public boolean isInsertCompiled() {
        return requestData.isInsertCompiled();
    }

    public boolean isUpdateCompiled() {
        return requestData.isUpdateCompiled();
    }

    public void addSubRequest(String name, Request<?> request) {
        requestData.getSubRequests().put(name, request);
        request.setName(name);
        requestData.setUpdateCompiled(false);
        requestData.setInsertCompiled(false);
        requestData.setSelectCompiled(false);
    }

    public Map<String, Request<?>> getSubRequests() {
        return requestData.getSubRequests();
    }

    public String getSelectSql() {
        return requestData.getCompiledSelectQuery();
    }

    public String getUpdateSql() {
        return requestData.getCompiledUpdateQuery();
    }

    public String getInsertSql() {
        return requestData.getCompiledInsertQuery();
    }

    public List<Selector<?>> getSelectSelectors() {
        return requestData.getSelectSelectors();
    }

    public List<Selector<?>> getJoinSelectors() {
        return requestData.getJoinSelectors();
    }

    public List<Selector<?>> getWhereSelectors() {
        return requestData.getWhereSelectors();
    }

    public List<Selector<?>> getFromSelectors() {
        return requestData.getFromSelectors();
    }

    public List<T> getResultList() {
        return (List<T>)Collections.unmodifiableList(requestData.getResultList());
    }

    public void clearResultList() {
        this.requestData.getResultList().clear();
    }

    public void addObject(T object) {
    	this.requestData.getResultList().add(object);
    }
    
    @SuppressWarnings("unchecked")
	public void addObjectUnchecked(Object object) {
    	if(requestData.getClass().isInstance(object))
    		this.requestData.getResultList().add((T)object);
    	else
    		throw new ClassCastException("object is not of class T = " + requestData.getClass());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Class<T> getKlass() {
        return requestData.getKlass();
    }

    public void setSelectSqlCompiler(SqlCompiler<T> sqlCompiler) {
        selectSqlCompiler = sqlCompiler;
    }

    public void setInsertSqlCompiler(SqlCompiler<T> sqlCompiler) {
        insertSqlCompiler = sqlCompiler;
    }

    public void setUpdateSqlCompiler(SqlCompiler<T> sqlCompiler) {
        updateSqlCompiler = sqlCompiler;
    }

    private void createSelectorLists() {
        requestData.getCompSelectList().clear();
        requestData.getCompJoinList().clear();
        requestData.getCompWhereList().clear();
        requestData.getCompFromList().clear();
        requestData.getSelectSelectors().clear();
        requestData.getJoinSelectors().clear();
        requestData.getWhereSelectors().clear();
        requestData.getFromSelectors().clear();

        for(Selector<?> selector : requestData.getSelectorList()) {
            if(selector.selectField() != null)
                requestData.getSelectSelectors().add(selector);
            if(selector.leftJoinSide() != null && selector.rightJoinSide() != null)
                requestData.getJoinSelectors().add(selector);
            if(selector.whereField() != null)
                requestData.getWhereSelectors().add(selector);
            if(selector.tableField() != null)
                requestData.getFromSelectors().add(selector);
            if(selector.limitField() != null)
                requestData.setLimitSelector(selector);
            if(selector.orderField() != null)
                requestData.setOrderBySelector(selector);
        }
        requestData.getCompSelectList().addAll(requestData.getSelectSelectors());
        requestData.getCompJoinList().addAll(requestData.getJoinSelectors());
        requestData.getCompWhereList().addAll(requestData.getWhereSelectors());
        requestData.getCompFromList().addAll(requestData.getFromSelectors());

        for(Map.Entry<String, Request<?>> entry : requestData.getSubRequests().entrySet()) {
            if(!entry.getValue().isSelectCompiled())
                entry.getValue().compileSelect();

            requestData.getCompSelectList().addAll(entry.getValue().getSelectSelectors());
            requestData.getCompJoinList().addAll(entry.getValue().getJoinSelectors());
            requestData.getCompWhereList().addAll(entry.getValue().getWhereSelectors());
            requestData.getCompFromList().addAll(entry.getValue().getFromSelectors());
        }

    }
}

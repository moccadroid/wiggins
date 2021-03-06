package org.lalelu.wiggins.requests.sql;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.lalelu.wiggins.data.assembler.DataObjectAssembler;
import org.lalelu.wiggins.requests.sql.compiler.InsertSqlCompiler;
import org.lalelu.wiggins.requests.sql.compiler.SelectSqlCompiler;
import org.lalelu.wiggins.requests.sql.compiler.SqlCompiler;
import org.lalelu.wiggins.requests.sql.compiler.UpdateSqlCompiler;
import org.lalelu.wiggins.selectors.sql.SqlSelector;

public class SqlRequest<T> {

    SqlRequestData<T> requestData = new SqlRequestData<T>();

    private String name = "";

    private SqlCompiler<T> selectSqlCompiler = null;
    private SqlCompiler<T> insertSqlCompiler = null;
    private SqlCompiler<T> updateSqlCompiler = null;

    public SqlRequest(Class<T> klass) {
        requestData.setKlass(klass);
        selectSqlCompiler = new SelectSqlCompiler<T>();
        insertSqlCompiler = new InsertSqlCompiler<T>();
        updateSqlCompiler = new UpdateSqlCompiler<T>();
    }

    public void addSelector(SqlSelector<?> selector) {
        requestData.getSelectorList().add(selector);
        requestData.setSelectCompiled(false);
        requestData.setInsertCompiled(false);
        requestData.setUpdateCompiled(false);
    }

    public List<SqlSelector<?>> getSelectors() {
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

    public void addSubRequest(String methodName, SqlRequest<?> request) {
        requestData.getSubRequests().put(methodName, request);
        request.setName(methodName);
        requestData.setUpdateCompiled(false);
        requestData.setInsertCompiled(false);
        requestData.setSelectCompiled(false);
    }

    public void addSubRequest(String setMethodName, String getMethodName, SqlRequest<?> request, boolean isMany) {
        request.setSetMethodName(setMethodName);
        request.setGetMethodName(getMethodName);
        request.setMany(isMany);
        requestData.getSubRequests().put(setMethodName, request);
        requestData.setUpdateCompiled(false);
        requestData.setInsertCompiled(false);
        requestData.setSelectCompiled(false);
    }

    public void setSetMethodName(String setMethodName) {
        this.requestData.setSetMethodName(setMethodName);
    }

    public void setGetMethodName(String getMethodName) {
        this.requestData.setGetMethodName(getMethodName);
    }

    public String getSetMethodName() {
        return requestData.getSetMethodName();
    }

    public String getGetMethodName() {
        return requestData.getGetMethodName();
    }

    public void setMany(boolean many) {
        this.requestData.setMany(many);
    }

    public boolean isMany() {
        return requestData.isMany();
    }

    public Map<String, SqlRequest<?>> getSubRequests() {
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

    public List<SqlSelector<?>> getSelectSelectors() {
        return requestData.getSelectSelectors();
    }

    public List<SqlSelector<?>> getJoinSelectors() {
        return requestData.getJoinSelectors();
    }

    public List<SqlSelector<?>> getWhereSelectors() {
        return requestData.getWhereSelectors();
    }

    public List<SqlSelector<?>> getFromSelectors() {
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
    
    public T assembleAndAddObject(DataObjectAssembler assembler) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
   		T object = assembler.<T>assembleObject(this);

        // TODO: this should be very slow... change! Maybe a Map for constant access?
        if(!requestData.getResultList().contains(object))
       	    this.requestData.getResultList().add(object);

       	return object;
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

        for(SqlSelector<?> selector : requestData.getSelectorList()) {
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

        for(Map.Entry<String, SqlRequest<?>> entry : requestData.getSubRequests().entrySet()) {
            if(!entry.getValue().isSelectCompiled())
                entry.getValue().compileSelect();

            requestData.getCompSelectList().addAll(entry.getValue().getSelectSelectors());
            requestData.getCompJoinList().addAll(entry.getValue().getJoinSelectors());
            requestData.getCompWhereList().addAll(entry.getValue().getWhereSelectors());
            requestData.getCompFromList().addAll(entry.getValue().getFromSelectors());
        }

    }
}

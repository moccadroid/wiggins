package org.lalelu.wiggins.requests.sql;

import org.lalelu.wiggins.selectors.sql.SqlLimitSelector;
import org.lalelu.wiggins.selectors.sql.SqlOrderBySelector;
import org.lalelu.wiggins.selectors.sql.SqlSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlRequestData<T> {
    private List<SqlSelector<?>> selectorList = new ArrayList<SqlSelector<?>>();

    private List<SqlSelector<?>> selectSelectors = new ArrayList<SqlSelector<?>>();
    private List<SqlSelector<?>> joinSelectors = new ArrayList<SqlSelector<?>>();
    private List<SqlSelector<?>> whereSelectors = new ArrayList<SqlSelector<?>>();
    private List<SqlSelector<?>> fromSelectors = new ArrayList<SqlSelector<?>>();

    private List<SqlSelector<?>> compSelectList = new ArrayList<SqlSelector<?>>();
    private List<SqlSelector<?>> compJoinList = new ArrayList<SqlSelector<?>>();
    private List<SqlSelector<?>> compWhereList = new ArrayList<SqlSelector<?>>();
    private List<SqlSelector<?>> compFromList = new ArrayList<SqlSelector<?>>();
    private SqlSelector<?> limitSelector = new SqlLimitSelector<Object>("");
    private SqlSelector<?> orderBySelector = new SqlOrderBySelector<Object>("", "");

    private Class<T> klass;

    private boolean isSelectCompiled = false;
    private boolean isInsertCompiled = false;
    private boolean isUpdateCompiled = false;

    private String compiledSelectQuery = "";
    private String compiledInsertQuery = "";
    private String compiledUpdateQuery = "";

    private String getMethodName = null;
    private String setMethodName = null;

    private boolean isMany = false;

    private List<T> resultList = new ArrayList<T>();

    private Map<String, SqlRequest<?>> subRequests = new HashMap<String, SqlRequest<?>>();

    public List<SqlSelector<?>> getSelectSelectors() {
        return selectSelectors;
    }

    public void setSelectSelectors(List<SqlSelector<?>> selectSelectors) {
        this.selectSelectors = selectSelectors;
    }

    public List<SqlSelector<?>> getJoinSelectors() {
        return joinSelectors;
    }

    public void setJoinSelectors(List<SqlSelector<?>> joinSelectors) {
        this.joinSelectors = joinSelectors;
    }

    public List<SqlSelector<?>> getWhereSelectors() {
        return whereSelectors;
    }

    public void setWhereSelectors(List<SqlSelector<?>> whereSelectors) {
        this.whereSelectors = whereSelectors;
    }

    public List<SqlSelector<?>> getFromSelectors() {
        return fromSelectors;
    }

    public void setFromSelectors(List<SqlSelector<?>> fromSelectors) {
        this.fromSelectors = fromSelectors;
    }

    public List<SqlSelector<?>> getCompSelectList() {
        return compSelectList;
    }

    public void setCompSelectList(List<SqlSelector<?>> compSelectList) {
        this.compSelectList = compSelectList;
    }

    public List<SqlSelector<?>> getCompJoinList() {
        return compJoinList;
    }

    public void setCompJoinList(List<SqlSelector<?>> compJoinList) {
        this.compJoinList = compJoinList;
    }

    public List<SqlSelector<?>> getCompWhereList() {
        return compWhereList;
    }

    public void setCompWhereList(List<SqlSelector<?>> compWhereList) {
        this.compWhereList = compWhereList;
    }

    public List<SqlSelector<?>> getCompFromList() {
        return compFromList;
    }

    public void setCompFromList(List<SqlSelector<?>> compFromList) {
        this.compFromList = compFromList;
    }

    public SqlSelector<?> getLimitSelector() {
        return limitSelector;
    }

    public void setLimitSelector(SqlSelector<?> limitSelector) {
        this.limitSelector = limitSelector;
    }

    public SqlSelector<?> getOrderBySelector() {
        return orderBySelector;
    }

    public void setOrderBySelector(SqlSelector<?> orderBySelector) {
        this.orderBySelector = orderBySelector;
    }

    public Class<T> getKlass() {
        return klass;
    }

    public void setKlass(Class<T> klass) {
        this.klass = klass;
    }

    public List<SqlSelector<?>> getSelectorList() {
        return selectorList;
    }

    public void setSelectorList(List<SqlSelector<?>> selectorList) {
        this.selectorList = selectorList;
    }

    public boolean isSelectCompiled() {
        return isSelectCompiled;
    }

    public void setSelectCompiled(boolean isSelectCompiled) {
        this.isSelectCompiled = isSelectCompiled;
    }

    public boolean isInsertCompiled() {
        return isInsertCompiled;
    }

    public void setInsertCompiled(boolean isInsertCompiled) {
        this.isInsertCompiled = isInsertCompiled;
    }

    public boolean isUpdateCompiled() {
        return isUpdateCompiled;
    }

    public void setUpdateCompiled(boolean isUpdateCompiled) {
        this.isUpdateCompiled = isUpdateCompiled;
    }

    public String getCompiledSelectQuery() {
        return compiledSelectQuery;
    }

    public void setCompiledSelectQuery(String compiledSelectQuery) {
        this.compiledSelectQuery = compiledSelectQuery;
    }

    public String getCompiledInsertQuery() {
        return compiledInsertQuery;
    }

    public void setCompiledInsertQuery(String compiledInsertQuery) {
        this.compiledInsertQuery = compiledInsertQuery;
    }

    public String getCompiledUpdateQuery() {
        return compiledUpdateQuery;
    }

    public void setCompiledUpdateQuery(String compiledUpdateQuery) {
        this.compiledUpdateQuery = compiledUpdateQuery;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public Map<String, SqlRequest<?>> getSubRequests() {
        return subRequests;
    }

    public void setSubRequests(Map<String, SqlRequest<?>> subRequests) {
        this.subRequests = subRequests;
    }

    public String getGetMethodName() {
        return getMethodName;
    }

    public void setGetMethodName(String getMethodName) {
        this.getMethodName = getMethodName;
    }

    public String getSetMethodName() {
        return setMethodName;
    }

    public void setSetMethodName(String setMethodName) {
        this.setMethodName = setMethodName;
    }

    public boolean isMany() {
        return isMany;
    }

    public void setMany(boolean isMany) {
        this.isMany = isMany;
    }
}

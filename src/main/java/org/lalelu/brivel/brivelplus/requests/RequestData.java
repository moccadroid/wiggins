package org.lalelu.brivel.brivelplus.requests;

import org.lalelu.brivel.brivelplus.selectors.LimitSelector;
import org.lalelu.brivel.brivelplus.selectors.OrderBySelector;
import org.lalelu.brivel.brivelplus.selectors.Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestData<T> {
    private List<Selector<?>> selectorList = new ArrayList<Selector<?>>();

    private List<Selector<?>> selectSelectors = new ArrayList<Selector<?>>();
    private List<Selector<?>> joinSelectors = new ArrayList<Selector<?>>();
    private List<Selector<?>> whereSelectors = new ArrayList<Selector<?>>();
    private List<Selector<?>> fromSelectors = new ArrayList<Selector<?>>();

    private List<Selector<?>> compSelectList = new ArrayList<Selector<?>>();
    private List<Selector<?>> compJoinList = new ArrayList<Selector<?>>();
    private List<Selector<?>> compWhereList = new ArrayList<Selector<?>>();
    private List<Selector<?>> compFromList = new ArrayList<Selector<?>>();
    private Selector<?> limitSelector = new LimitSelector<Object>("");
    private Selector<?> orderBySelector = new OrderBySelector<Object>("", "");

    private Class<T> klass;

    private boolean isSelectCompiled = false;
    private boolean isInsertCompiled = false;
    private boolean isUpdateCompiled = false;

    private String compiledSelectQuery = "";
    private String compiledInsertQuery = "";
    private String compiledUpdateQuery = "";

    private List<T> resultList = new ArrayList<T>();

    private Map<String, Request<?>> subRequests = new HashMap<String, Request<?>>();

    public List<Selector<?>> getSelectSelectors() {
        return selectSelectors;
    }

    public void setSelectSelectors(List<Selector<?>> selectSelectors) {
        this.selectSelectors = selectSelectors;
    }

    public List<Selector<?>> getJoinSelectors() {
        return joinSelectors;
    }

    public void setJoinSelectors(List<Selector<?>> joinSelectors) {
        this.joinSelectors = joinSelectors;
    }

    public List<Selector<?>> getWhereSelectors() {
        return whereSelectors;
    }

    public void setWhereSelectors(List<Selector<?>> whereSelectors) {
        this.whereSelectors = whereSelectors;
    }

    public List<Selector<?>> getFromSelectors() {
        return fromSelectors;
    }

    public void setFromSelectors(List<Selector<?>> fromSelectors) {
        this.fromSelectors = fromSelectors;
    }

    public List<Selector<?>> getCompSelectList() {
        return compSelectList;
    }

    public void setCompSelectList(List<Selector<?>> compSelectList) {
        this.compSelectList = compSelectList;
    }

    public List<Selector<?>> getCompJoinList() {
        return compJoinList;
    }

    public void setCompJoinList(List<Selector<?>> compJoinList) {
        this.compJoinList = compJoinList;
    }

    public List<Selector<?>> getCompWhereList() {
        return compWhereList;
    }

    public void setCompWhereList(List<Selector<?>> compWhereList) {
        this.compWhereList = compWhereList;
    }

    public List<Selector<?>> getCompFromList() {
        return compFromList;
    }

    public void setCompFromList(List<Selector<?>> compFromList) {
        this.compFromList = compFromList;
    }

    public Selector<?> getLimitSelector() {
        return limitSelector;
    }

    public void setLimitSelector(Selector<?> limitSelector) {
        this.limitSelector = limitSelector;
    }

    public Selector<?> getOrderBySelector() {
        return orderBySelector;
    }

    public void setOrderBySelector(Selector<?> orderBySelector) {
        this.orderBySelector = orderBySelector;
    }

    public Class<T> getKlass() {
        return klass;
    }

    public void setKlass(Class<T> klass) {
        this.klass = klass;
    }

    public List<Selector<?>> getSelectorList() {
        return selectorList;
    }

    public void setSelectorList(List<Selector<?>> selectorList) {
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

    public Map<String, Request<?>> getSubRequests() {
        return subRequests;
    }

    public void setSubRequests(Map<String, Request<?>> subRequests) {
        this.subRequests = subRequests;
    }
}

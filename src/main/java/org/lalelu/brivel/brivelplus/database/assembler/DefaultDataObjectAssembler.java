package org.lalelu.brivel.brivelplus.database.assembler;

import org.lalelu.brivel.brivelplus.selectors.Selector;

import java.util.List;

public abstract class DefaultDataObjectAssembler implements DataObjectAssembler {
    protected List<Selector<?>> selectorList = null;
    protected Object[] values = null;

    public void setValues(Object[] values) {
        this.values = values;
    }

    public void setSelectorList(List<Selector<?>> selectorList) {
        this.selectorList = selectorList;
    }
}

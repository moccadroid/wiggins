package org.lalelu.wiggins.data.assembler.sql;

import org.lalelu.wiggins.data.assembler.DataObjectAssembler;
import org.lalelu.wiggins.selectors.sql.Selector;

import java.util.List;

public abstract class SqlDefaultDataObjectAssembler implements DataObjectAssembler {
    protected List<Selector<?>> selectorList = null;
    protected Object[] values = null;

    public void setValues(Object[] values) {
        this.values = values;
    }

    public void setSelectorList(List<Selector<?>> selectorList) {
        this.selectorList = selectorList;
    }
}

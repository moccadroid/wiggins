package org.lalelu.brivel.brivelplus.selectors.dataconverter;

import java.math.BigDecimal;

public class BigDecimalDataConverter extends DefaultDataConverter {
    @Override
    public BigDecimal read(Object object) {
        return new BigDecimal(String.valueOf(object));
    }
}

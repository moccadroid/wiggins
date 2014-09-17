package org.lalelu.brivel.brivelplus.selectors.dataconverter;

import java.math.BigDecimal;

public class BigDecimalDataConverter extends DefaultDataConverter {
    @Override
    public Object read(Object object) {
        Double decimal = Double.parseDouble(""+object);
        return new BigDecimal(decimal);
    }
}

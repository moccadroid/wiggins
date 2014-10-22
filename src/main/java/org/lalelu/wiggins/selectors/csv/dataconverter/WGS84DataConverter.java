package org.lalelu.wiggins.selectors.csv.dataconverter;

import java.math.BigDecimal;

public class WGS84DataConverter extends DefaultDataConverter {
    @Override
    public Object read(Object object) {
        BigDecimal coord = new BigDecimal(0);
        try {
            if (object instanceof String) {
                String strCoord = ((String) object).replace(",", ".");
                coord = new BigDecimal(strCoord);
            }
        } catch (NumberFormatException e) {

        }
        return coord;
    }
}

package org.lalelu.wiggins.selectors.dataconverter;

public class CombinedDataConverter extends DefaultDataConverter {

    private String separator = "";

    public CombinedDataConverter(String separator) {
        this.separator = separator;
    }

    @Override
    public Object readCombined(Object firstObject, Object secondObject) {
        if(firstObject == null) {
            return secondObject;
        }
        if(firstObject instanceof String && secondObject instanceof String) {
            return firstObject + separator + secondObject;
        }

        return firstObject;
    }
}

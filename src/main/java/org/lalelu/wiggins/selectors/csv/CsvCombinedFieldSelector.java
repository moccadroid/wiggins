package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.csv.dataconverter.CombinedDataConverter;

public class CsvCombinedFieldSelector extends CsvFieldSelector {

    public CsvCombinedFieldSelector(String csvField, String objectField, String separator, Class<?> fieldType) {
        super(csvField, objectField, fieldType, new CombinedDataConverter(separator));
    }
}

package org.lalelu.wiggins.selectors.csv;

import org.lalelu.wiggins.selectors.csv.dataconverter.DataConverter;

public interface CsvSelector {

    public DataConverter getDataConverter();

    public String getCsvField();

    public String getPrefix();

    public String getObjectField();

    public Class getFieldType();
}

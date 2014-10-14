package org.lalelu.wiggins.data.csvparser;

import java.util.ArrayList;
import java.util.List;

public class DefaultCsvParser implements CsvParser {
    @Override
    public List<String[]> parseCsv(String csvString) {
        return new ArrayList<String[]>();
    }
}

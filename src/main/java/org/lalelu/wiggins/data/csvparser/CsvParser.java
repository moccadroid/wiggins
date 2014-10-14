package org.lalelu.wiggins.data.csvparser;

import java.util.ArrayList;
import java.util.List;

public interface CsvParser {

    public List<String[]> parseCsv(String csvString);
}

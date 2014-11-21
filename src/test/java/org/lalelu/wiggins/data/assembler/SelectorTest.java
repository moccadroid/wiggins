package org.lalelu.wiggins.data.assembler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lalelu.wiggins.selectors.csv.CsvCombinedFieldSelector;
import org.lalelu.wiggins.selectors.csv.CsvDefaultValueSelector;
import org.lalelu.wiggins.selectors.csv.CsvFieldSelector;
import org.lalelu.wiggins.selectors.csv.CsvIndexSelector;
import org.lalelu.wiggins.selectors.dataconverter.*;

public class SelectorTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testCsvSelectors() {
        CsvFieldSelector csvFieldSelector1 = new CsvFieldSelector("csvField", "objectField", String.class);
        CsvFieldSelector csvFieldSelector4 = new CsvFieldSelector("csvField", "objectField", String.class, new DoubleDataConverter());
        CsvFieldSelector csvFieldSelector2 = new CsvFieldSelector("csvField", "objectField", "prefix1_", Double.class);
        CsvFieldSelector csvFieldSelector3 = new CsvFieldSelector("csvField", "objectField", "prefix2_", String.class, new DefaultDataConverter());

        CsvCombinedFieldSelector csvCombinedFieldSelector = new CsvCombinedFieldSelector("csvField", "objectField", ", ", String.class);

        CsvDefaultValueSelector csvDefaultValueSelector = new CsvDefaultValueSelector("objectField", String.class, "defaultValue");

        CsvIndexSelector csvIndexSelector1 = new CsvIndexSelector(0, "objectField", Long.class);
        CsvIndexSelector csvIndexSelector2 = new CsvIndexSelector(1, "objectField", String.class, new DoubleDataConverter());

        // csvFieldSelector1:
        Assert.assertEquals(csvFieldSelector1.getSelectorPath(), "csvField");
        Assert.assertEquals(csvFieldSelector1.getObjectField(), "ObjectField");
        Assert.assertEquals(csvFieldSelector1.getFieldType(), String.class);
        Assert.assertEquals(csvFieldSelector1.getPrefix(), "");
        Assert.assertEquals(csvFieldSelector1.getDataConverter().getClass(), StringDataConverter.class);

        // csvFieldSelector2:
        Assert.assertEquals(csvFieldSelector2.getSelectorPath(), "csvField");
        Assert.assertEquals(csvFieldSelector2.getObjectField(), "ObjectField");
        Assert.assertEquals(csvFieldSelector2.getFieldType(), Double.class);
        Assert.assertEquals(csvFieldSelector2.getPrefix(), "prefix1_");
        Assert.assertEquals(csvFieldSelector2.getDataConverter().getClass(), DoubleDataConverter.class);

        // csvFieldSelector3:
        Assert.assertEquals(csvFieldSelector3.getSelectorPath(), "csvField");
        Assert.assertEquals(csvFieldSelector3.getObjectField(), "ObjectField");
        Assert.assertEquals(csvFieldSelector3.getFieldType(), String.class);
        Assert.assertEquals(csvFieldSelector3.getPrefix(), "prefix2_");
        Assert.assertEquals(csvFieldSelector3.getDataConverter().getClass(), DefaultDataConverter.class);

        // csvFieldSelector4:
        Assert.assertEquals(csvFieldSelector4.getSelectorPath(), "csvField");
        Assert.assertEquals(csvFieldSelector4.getObjectField(), "ObjectField");
        Assert.assertEquals(csvFieldSelector4.getFieldType(), String.class);
        Assert.assertEquals(csvFieldSelector4.getPrefix(), "");
        Assert.assertEquals(csvFieldSelector4.getDataConverter().getClass(), DoubleDataConverter.class);

        // csvCombinedFieldSelector
        Assert.assertEquals(csvCombinedFieldSelector.getSelectorPath(), "csvField");
        Assert.assertEquals(csvCombinedFieldSelector.getObjectField(), "ObjectField");
        Assert.assertEquals(csvCombinedFieldSelector.getFieldType(), String.class);
        Assert.assertEquals(csvCombinedFieldSelector.getPrefix(), "");
        Assert.assertEquals(csvCombinedFieldSelector.getDataConverter().readCombined("test", "test"), "test, test");
        Assert.assertEquals(csvCombinedFieldSelector.getDataConverter().getClass(), CombinedDataConverter.class);

        // csvDefaultValueSelector
        Assert.assertEquals(csvDefaultValueSelector.getSelectorPath(), null);
        Assert.assertEquals(csvDefaultValueSelector.getObjectField(), "ObjectField");
        Assert.assertEquals(csvDefaultValueSelector.getFieldType(), String.class);
        Assert.assertEquals(csvDefaultValueSelector.getPrefix(), "");
        Assert.assertEquals(csvDefaultValueSelector.getDataConverter().read("test"), "defaultValue");
        Assert.assertEquals(csvDefaultValueSelector.getDataConverter().getClass(), DefaultValueConverter.class);

        // csvIndexSelector1:
        Assert.assertEquals(csvIndexSelector1.getSelectorPath(), "0");
        Assert.assertEquals(csvIndexSelector1.getObjectField(), "ObjectField");
        Assert.assertEquals(csvIndexSelector1.getFieldType(), Long.class);
        Assert.assertEquals(csvIndexSelector1.getPrefix(), "");
        Assert.assertEquals(csvIndexSelector1.getDataConverter().getClass(), LongDataConverter.class);

        // csvIndexSelector2:
        Assert.assertEquals(csvIndexSelector2.getSelectorPath(), "1");
        Assert.assertEquals(csvIndexSelector2.getObjectField(), "ObjectField");
        Assert.assertEquals(csvIndexSelector2.getFieldType(), String.class);
        Assert.assertEquals(csvIndexSelector2.getPrefix(), "");
        Assert.assertEquals(csvIndexSelector2.getDataConverter().getClass(), DoubleDataConverter.class);
    }

    @Test
    public void testJsonSelectors() {

    }
}

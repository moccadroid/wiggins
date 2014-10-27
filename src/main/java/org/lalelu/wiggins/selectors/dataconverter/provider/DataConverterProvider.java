package org.lalelu.wiggins.selectors.dataconverter.provider;

import org.lalelu.wiggins.selectors.dataconverter.*;

import java.util.HashMap;
import java.util.Map;

public class DataConverterProvider {
    private static Map<Class<?>, DataConverter> converterMap = new HashMap<Class<?>, DataConverter>();
    static {
        converterMap.put(String.class, new StringDataConverter());
        converterMap.put(Long.class, new LongDataConverter());
        converterMap.put(Double.class, new DoubleDataConverter());
        converterMap.put(Boolean.class, new BooleanDataConverter());
    }

    public static DataConverter getDefaultDataConverter(Class<?> clazz) {
        DataConverter dataConverter = converterMap.get(clazz);

        if(dataConverter == null)
            return new DefaultDataConverter();
        else
            return dataConverter;
    }
}

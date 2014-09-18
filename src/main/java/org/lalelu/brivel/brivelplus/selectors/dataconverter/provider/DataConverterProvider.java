package org.lalelu.brivel.brivelplus.selectors.dataconverter.provider;

import org.lalelu.brivel.brivelplus.selectors.dataconverter.*;

import java.util.HashMap;
import java.util.Map;

public class DataConverterProvider {
    private static Map<Class<?>, DataConverter> converterMap = new HashMap<Class<?>, DataConverter>();
    static {
        converterMap.put(String.class, new StringDataConverter());
        converterMap.put(Long.class, new LongDataConverter());
        converterMap.put(Double.class, new DoubleDataConverter());
    }

    public static DataConverter getDefaultDataConverter(Class<?> clazz) {
        DataConverter dataConverter = converterMap.get(clazz);

        if(dataConverter == null)
            return new DefaultDataConverter();
        else
            return dataConverter;
    }
}

package org.lalelu.wiggins.selectors.dataconverter;

public class LongDataConverter extends DefaultDataConverter {
    @Override
    public Long read(Object object) {
        return Long.valueOf(String.valueOf(object));
    }

    @Override
    public Long write(Object object) {
        if(object == null)
            return 0L;
        return Long.valueOf(String.valueOf(object));
    }
}

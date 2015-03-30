package org.lalelu.wiggins.selectors.dataconverter;

public class LongDataConverter extends DefaultDataConverter {
    @Override
    public Long read(Object object) {
        if(object == null)
            return 0L;
        if(object instanceof String) {
            String tmp = (String) object;
            return Long.valueOf(tmp.trim());
        }
        return 0L;
    }

    @Override
    public Long write(Object object) {
        if(object == null)
            return 0L;
        if(object instanceof String) {
            String tmp = (String) object;
            return Long.valueOf(tmp.trim());
        }
        return 0L;
    }
}

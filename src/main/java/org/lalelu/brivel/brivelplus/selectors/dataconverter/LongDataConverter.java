package org.lalelu.brivel.brivelplus.selectors.dataconverter;

public class LongDataConverter extends DefaultDataConverter {
    @Override
    public Object read(Object object) {
        return Long.valueOf(""+object);
    }

    @Override
    public Object write(Object object) {
        if(object == null)
            return 0L;
        return Long.valueOf(""+object);
    }
}

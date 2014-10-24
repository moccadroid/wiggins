package org.lalelu.wiggins.errors;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandler {
    private Map<String, Exception> exceptionMap = new HashMap<String, Exception>();

    public ExceptionHandler() {

    }

    public void addException(Exception e) {
        exceptionMap.put(e.getClass().getSimpleName(), e);
    }

    public boolean hasException(String exceptionName) {
        return exceptionMap.containsKey(exceptionName);
    }

    public Exception getException(String exceptionName) {
        return exceptionMap.get(exceptionName);
    }
}

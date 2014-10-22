package org.lalelu.wiggins.errors;

import org.lalelu.wiggins.WigginsCentral;

import java.util.ArrayList;
import java.util.List;

public class ExceptionPool {
    private static ExceptionPool instance;
    private List<Exception> exceptionList = null;
    private List<ExceptionListener> exceptionListenerList = null;

    private ExceptionPool() {
        exceptionList = new ArrayList<Exception>();
        exceptionListenerList = new ArrayList<ExceptionListener>();
    }

    public static ExceptionPool getInstance() {
        if(instance == null)
            instance = new ExceptionPool();

        return instance;
    }

    public void addException(Exception e) {
        if(WigginsCentral.isPrintStackTrace())
            e.printStackTrace();

        for(ExceptionListener listener : exceptionListenerList) {
            listener.handleException(e);
        }

        exceptionList.add(e);
    }

    public List<Exception> getExceptionList() {
        return exceptionList;
    }

    public void addListener(ExceptionListener listener) {
        exceptionListenerList.add(listener);
    }

    public void removeListener(ExceptionListener listener) {
        exceptionListenerList.remove(listener);
    }
}

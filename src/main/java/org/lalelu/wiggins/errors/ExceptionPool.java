package org.lalelu.wiggins.errors;

import org.lalelu.wiggins.WigginsCentral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExceptionPool {
    private static ExceptionPool instance;
    private List<Exception> exceptionList = null;
    private List<ExceptionListener> exceptionListenerList = null;
    private Map<String, Exception> exceptionMap = null;

    private Map<Object, ExceptionHandler> registeredRequests = null;

    private Map<String, ExceptionHandler> exceptionHandlerMap = null;

    private ExceptionPool() {
        exceptionList = new ArrayList<Exception>();
        exceptionListenerList = new ArrayList<ExceptionListener>();
        exceptionMap = new HashMap<String, Exception>();

        exceptionHandlerMap = new HashMap<String, ExceptionHandler>();

        registeredRequests = new HashMap<Object, ExceptionHandler>();
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

        exceptionMap.put(e.getClass().getSimpleName(), e);
        exceptionList.add(e);
    }

    public ExceptionHandler getExceptionHandler(Object caller) {
        return registeredRequests.get(caller);
    }

    public void addException(Exception e, Object caller) {
        if(WigginsCentral.isPrintStackTrace())
            e.printStackTrace();

        ExceptionHandler handler = registeredRequests.get(caller);
        if(handler != null) {
            handler.addException(e);
        }
    }

    public void register(Object request) {
        registeredRequests.put(request, new ExceptionHandler());
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

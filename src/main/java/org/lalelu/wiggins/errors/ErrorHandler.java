package org.lalelu.wiggins.errors;

import java.lang.reflect.InvocationTargetException;

public class ErrorHandler {

    public static String resolveException(Exception exception, boolean printStackTrace) {
        String errorMessage = "";

        if(exception instanceof IllegalAccessException) {
            errorMessage = "IllegalAccessException";
        }
        if(exception instanceof InvocationTargetException) {
            errorMessage = "InvocationTargetException";
        }
        if(exception instanceof NoSuchMethodException) {
            errorMessage = "NoSuchMethodException";
        }
        if(exception instanceof InstantiationException) {
            errorMessage = "InstantiationException";
        }
        if(exception instanceof Exception) {
            errorMessage = exception.getClass() + "";
        }

        errorMessage += "\n\n" + exception.toString();

        if(printStackTrace)
            exception.printStackTrace();
        else
            System.out.println(errorMessage);

        return errorMessage;
    }
}

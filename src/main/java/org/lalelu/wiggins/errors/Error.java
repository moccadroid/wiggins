package org.lalelu.wiggins.errors;

public class Error {
    private Exception exception = null;
    private String hint = "";

    public Error(String hint) {
        this.hint = hint;
    }

    public Error(String hint, Exception exception) {
        this.hint = hint;
        this.exception = exception;
    }

    public String hint() {
        return hint;
    }

    public Exception getException() {
        return null;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}

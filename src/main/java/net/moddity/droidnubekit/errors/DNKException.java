package net.moddity.droidnubekit.errors;

/**
 * Created by jaume on 19/6/15.
 */
public class DNKException extends Exception {

    private int errorCode;
    private Throwable exception;

    public DNKException(int errorCode, Throwable exception) {
        this.errorCode = errorCode;
        this.exception = exception;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}

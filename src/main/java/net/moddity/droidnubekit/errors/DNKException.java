package net.moddity.droidnubekit.errors;

/**
 * Created by jaume on 19/6/15.
 */
public class DNKException extends Exception {

    private int errorCode;

    public DNKException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

package edu.asu.diging.scriptoocloud.core.exceptions;


public class DataFileNotFoundException extends Exception {
    public DataFileNotFoundException(String message) {
        super(message);
    }

    public DataFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

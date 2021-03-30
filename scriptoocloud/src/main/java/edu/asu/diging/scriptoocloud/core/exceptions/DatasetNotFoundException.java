package edu.asu.diging.scriptoocloud.core.exceptions;


public class DatasetNotFoundException extends Exception {

    public DatasetNotFoundException(String message) {
        super(message);
    }

    public DatasetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

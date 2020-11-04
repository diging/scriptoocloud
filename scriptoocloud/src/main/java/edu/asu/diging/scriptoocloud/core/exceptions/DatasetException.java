package edu.asu.diging.scriptoocloud.core.exceptions;

public class DatasetException extends RuntimeException {

    public DatasetException(String message) {
        super(message);
    }

    public DatasetException(String message, Throwable cause) {
        super(message, cause);
    }
}

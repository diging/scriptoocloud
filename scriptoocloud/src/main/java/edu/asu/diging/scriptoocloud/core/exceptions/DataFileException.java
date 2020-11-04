package edu.asu.diging.scriptoocloud.core.exceptions;

public class DataFileException extends RuntimeException {

    public DataFileException(String message) {
        super(message);
    }

    public DataFileException(String message, Throwable cause) {
        super(message, cause);
    }
}

package edu.asu.diging.scriptoocloud.core.exceptions;

public class DataFileStorageException extends RuntimeException {

    public DataFileStorageException(String message) {
        super(message);
    }

    public DataFileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

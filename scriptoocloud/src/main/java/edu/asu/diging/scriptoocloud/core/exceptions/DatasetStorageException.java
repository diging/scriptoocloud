package edu.asu.diging.scriptoocloud.core.exceptions;

public class DatasetStorageException extends RuntimeException {

    public DatasetStorageException(String message) {
        super(message);
    }

    public DatasetStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
package edu.asu.diging.scriptoocloud.core.exceptions;


public class FileSystemStorageException extends Exception {
    public FileSystemStorageException(String message) {
        super(message);
    }

    public FileSystemStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

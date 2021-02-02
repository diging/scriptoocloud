package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;

import java.io.File;
import java.nio.file.Path;

public interface IFileSystemService {

    void addDirectories(String username, String type, String id) throws FileSystemStorageException;

    Path createPath(String username, String type, String id) throws FileSystemStorageException;

    void deleteDirectories(String username, String type, String id) throws FileSystemStorageException;

    boolean deleteDirectoryOrFile(File directoryToBeDeleted) throws FileSystemStorageException;

    void createFileInDirectory(String username, String type, String id, String filename,
                               byte[] bytes) throws FileSystemStorageException;
}
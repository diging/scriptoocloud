package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;

import java.io.File;
import java.nio.file.Path;

public interface IFileSystemService {

    void addDatasetDirectories(String id, String username) throws FileSystemStorageException;

    Path createPath(String username, String datasetName) throws FileSystemStorageException;

    void deleteDatasetDirectories(Long id, String username) throws FileSystemStorageException;

    boolean deleteDirectoryOrFile(File directoryToBeDeleted) throws FileSystemStorageException;

    void createFileInDirectory(String username, String datasetId, String filename,
                               byte[] bytes) throws FileSystemStorageException;
}
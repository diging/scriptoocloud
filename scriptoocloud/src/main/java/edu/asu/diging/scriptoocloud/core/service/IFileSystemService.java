package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;

import java.io.File;
import java.nio.file.Path;

public interface IFileSystemService {

    void addDatasetDirectories(String id, String username) throws DatasetStorageException;

    Path createPath(String username, String datasetName);

    void deleteDatasetDirectories(Long id, String username) throws SecurityException;

    boolean deleteDirectoryOrFile(File directoryToBeDeleted) throws DatasetStorageException;

    void createFileInDirectory(String username, String datasetId, String filename,
                               byte[] bytes) throws DataFileStorageException;
}

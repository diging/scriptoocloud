package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.service.IFileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;

@Service
@PropertySource("classpath:/config.properties")
public class FileSystemService implements IFileSystemService {

    @Value("${rootUploadLocation}")
    private String rootLocationString;

    @Autowired
    public FileSystemService() {
    }

    /**
     * Creates the dataset (and its required directories) on the filesystem.
     *
     * @param id       The id of the dataset.
     * @param username The username of the dataset.
     * @throws DatasetStorageException The exception thrown if the directory could not be created.
     */
    @Override
    public void addDatasetDirectories(String id, String username) throws DatasetStorageException {
        // First, the correct directory must be created
        Path path = createPath(username, id);
        File directory = path.toFile();
        // if the Path was created successfully, create the directory
        try {
            if (!directory.exists()) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new DatasetStorageException("IOException occurred in addDatasetDirectories", e);
        } catch (UnsupportedOperationException e) {
            throw new DatasetStorageException("UnsupportedOperationException occurred in addDatasetDirectories", e);
        } catch (SecurityException e) {
            throw new DatasetStorageException("SecurityException occurred in addDatasetDirectories", e);
        }
    }

    /**
     * A helper method to create a Path given a username and dataset name.
     *
     * @param username  The username of the owner of the dataset.
     * @param datasetId The id of the dataset.
     * @return Returns a Path.
     */
    @Override
    public Path createPath(String username, String datasetId) {
        Path path;
        if (datasetId != null) {
            try {
                path = Paths.get(this.rootLocationString, username, datasetId);
                String pathName = StringUtils.cleanPath(Objects.requireNonNull(path).toString());
                path = Paths.get(pathName);
                return path;
            } catch (InvalidPathException e) {
                throw new DatasetStorageException("An InvalidPathException occurred in createPath with null datasetName", e);
            }
        } else {
            try {
                path = Paths.get(this.rootLocationString, username);
                String pathName = StringUtils.cleanPath(Objects.requireNonNull(path).toString());
                path = Paths.get(pathName);
            } catch (InvalidPathException e) {
                throw new DatasetStorageException("An InvalidPathException occurred in createPath with datasetName present", e);
            }
            return path;
        }
    }

    /**
     * Deletes a dataset on the filesystem.
     *
     * @param id The id of the dataset.
     * @throws SecurityException throws a storage exception if the directory could not be deleted.
     */
    @Override
    public void deleteDatasetDirectories(Long id, String username) throws SecurityException {

        Path path = createPath(username, String.valueOf(id));
        File directory = path.toFile();
        try {
            deleteDirectory(directory);
        } catch (SecurityException e) {
            throw new DatasetStorageException("A SecurityException occurred in deleteDatasetDirectories", e);
        }
    }

    /**
     * Helper method to recursively delete directories.
     *
     * @param directoryToBeDeleted The directory / File to be deleted.
     * @return boolean indicating success of deleting directory.
     * @throws DatasetStorageException if directory cannot be deleted.
     */
    @Override
    public boolean deleteDirectory(File directoryToBeDeleted) throws DatasetStorageException {

        File[] allContents;
        try {
            allContents = directoryToBeDeleted.listFiles();
        } catch (SecurityException e) {
            throw new DatasetStorageException("A SecurityException occurred in deleteDirectory when generating file list", e);
        }
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        try {
            return directoryToBeDeleted.delete();
        } catch (SecurityException e) {
            throw new DatasetStorageException("A SecurityException occurred in deleteDirectory when deleting a directory", e);
        }
    }

    /**
     * Stores the user-uploaded file on the file system.
     *
     * @param username  The name (primary key) of the user / owner of the Dataset to which
     *                  the file belongs.
     * @param datasetId The id of the Dataset to which the file belongs.
     * @param filename  The name of the file to be stored.
     * @throws DataFileStorageException If the file could not be stored on the file system.
     */
    @Override
    public void createFileInDirectory(String username, String datasetId, String filename,
                                      byte[] bytes) throws DataFileStorageException {

        Path destinationFile = Paths.get(rootLocationString).resolve(
                Paths.get(username, datasetId, filename))
                .normalize().toAbsolutePath();
        // make sure path structure is: /serverRoot/username/datasetId/file
        if (!destinationFile.getParent().getParent().getParent()
                .equals(Paths.get(rootLocationString).toAbsolutePath())) {
            // This is a security check to make sure that the user cannot access
            // unauthorized areas of the file system - e.g. by naming a file: "../../file"
            throw new DataFileStorageException("Cannot store file at calculated location");
        }
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DataFileStorageException(
                    "An IO Error occurred while copying the file to its directory", e);
        } catch (SecurityException e) {
            throw new DataFileStorageException(
                    "A Security Exception occurred while copying the file to its directory", e);
        }
    }

}

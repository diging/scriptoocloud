package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
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
     * @throws FileSystemStorageException The exception thrown if the directory could not be created.
     */
    @Override
    public void addDatasetDirectories(String id, String username) throws FileSystemStorageException {
        // First, the correct directory must be created
        Path path = createPath(username, id);
        File directory = path.toFile();
        // if the Path was created successfully, create the directory
        try {
            if (!directory.exists()) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new FileSystemStorageException("IOException occurred when attempting to add the Dataset", e);
        } catch (UnsupportedOperationException e) {
            throw new FileSystemStorageException("UnsupportedOperationException occurred when attempting to add the Dataset", e);
        } catch (SecurityException e) {
            throw new FileSystemStorageException("SecurityException occurred when attempting to add the Dataset", e);
        }
    }

    /**
     * A helper method to create a Path given a username and dataset name.
     *
     * @param username  The username of the owner of the dataset.
     * @param directory The directory name.
     * @return Returns a Path.
     * @throws InvalidPathException Exception if the path cannot be created.
     */
    @Override
    public Path createPath(String username, String directory) throws InvalidPathException {
        Path path;
        if (directory != null) {
            path = Paths.get(this.rootLocationString, username, directory);
        } else {
            path = Paths.get(this.rootLocationString, username);
        }
        return Paths.get(StringUtils.cleanPath(Objects.requireNonNull(path).toString()));
    }

    /**
     * Deletes a dataset on the filesystem.
     *
     * @param id The id of the dataset.
     * @throws FileSystemStorageException throws an exception if the directory could not be deleted.
     */
    @Override
    public void deleteDatasetDirectories(Long id, String username) throws FileSystemStorageException {

        try {
            Path path = createPath(username, String.valueOf(id));
            File directory = path.toFile();
            deleteDirectoryOrFile(directory);
        } catch (InvalidPathException e) {
            throw new FileSystemStorageException(
                    "The path could not be found when attempting to delete the Dataset from the file system", e);
        } catch (SecurityException e) {
            throw new FileSystemStorageException(
                    "A SecurityException occurred when attempting to delete the Dataset from the file system", e);
        }
    }

    /**
     * Helper method to recursively delete directories.
     *
     * @param directoryToBeDeleted The directory / File to be deleted.
     * @return boolean indicating success of deleting directory.
     * @throws FileSystemStorageException if directory cannot be deleted.
     */
    @Override
    public boolean deleteDirectoryOrFile(File directoryToBeDeleted) throws
            FileSystemStorageException {

        File[] allContents;
        try {
            allContents = directoryToBeDeleted.listFiles();
        } catch (SecurityException e) {
            throw new FileSystemStorageException("A SecurityException occurred in when generating list of files to be deleted", e);
        }
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectoryOrFile(file);
            }
        }
        try {
            return directoryToBeDeleted.delete();
        } catch (SecurityException e) {
            throw new FileSystemStorageException("A SecurityException occurred when deleting a directory", e);
        }
    }

    /**
     * Stores the user-uploaded file on the file system.
     *
     * @param username  The name (primary key) of the user / owner of the Dataset to which
     *                  the file belongs.
     * @param datasetId The id of the Dataset to which the file belongs.
     * @param filename  The name of the file to be stored.
     * @throws FileSystemStorageException If the file could not be stored on the file system.
     */
    @Override
    public void createFileInDirectory(String username, String datasetId, String filename,
                                      byte[] bytes) throws FileSystemStorageException {

        Path destinationFile = Paths.get(rootLocationString).resolve(
                Paths.get(username, datasetId, filename))
                .normalize().toAbsolutePath();
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileSystemStorageException(
                    "An IO Error occurred while copying the file to its directory", e);
        } catch (SecurityException e) {
            throw new FileSystemStorageException(
                    "A Security Exception occurred while copying the file to its directory", e);
        }
    }

}
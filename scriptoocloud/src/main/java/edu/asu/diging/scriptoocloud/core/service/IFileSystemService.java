package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * The interface which provides methods for adding and deleting files on the server file system.
 */
public interface IFileSystemService {

    /**
     * Creates directories on the filesystem.
     *
     * @param username The username to be used as a directory name.
     * @param type     The directory type (e.g., Dataset, etc.)
     * @param id       The id to be used as a directory name.
     * @throws FileSystemStorageException The exception thrown if the directory could not be created.
     */
    void addDirectories(String username, String type, String id) throws FileSystemStorageException;

    /**
     * A helper method to create a Path given a username and unique id.
     *
     * @param username The username of the owner of the dataset.
     * @param type     The directory type (e.g., Dataset, etc.)
     * @param id       The id (can be null), if null the path created is of the form:
     *                 [root]/[username]/[type] as opposed to [root]/[username]/[type]/[id]
     * @return Returns a Path.
     * @throws InvalidPathException Exception if the path cannot be created.
     */
    Path createPath(String username, String type, String id) throws FileSystemStorageException;

    /**
     * Deletes a directory on the filesystem.
     *
     * @param username The name (primary key) of the user / owner of the Dataset
     * @param type     The directory type (e.g., Dataset, etc.)
     * @param id       The id of the type to which the file belongs.
     * @throws FileSystemStorageException throws an exception if the directory could not be deleted.
     */
    void deleteDirectories(String username, String type, String id) throws FileSystemStorageException;

    /**
     * Helper method to recursively delete directories.
     *
     * @param directoryToBeDeleted The directory / File to be deleted.
     * @return boolean indicating success of deleting directory.
     * @throws FileSystemStorageException if directory cannot be deleted.
     */
    boolean deleteDirectoryOrFile(File directoryToBeDeleted) throws FileSystemStorageException;

    /**
     * Stores the user-uploaded file on the file system in the form:
     * [username]/[directory_type]/[directory_type_id]/[filename]
     *
     * @param username The name (primary key) of the user / owner of the Dataset to which
     *                 the file belongs.
     * @param type     The directory type (e.g., Dataset, etc.)
     * @param id       The id of the type to which the file belongs.
     * @param filename The name of the file to be stored.
     * @param bytes    The array of bytes making up the file.
     * @throws FileSystemStorageException If the file could not be stored on the file system.
     */
    void createFileInDirectory(String username, String type, String id, String filename,
                               byte[] bytes) throws FileSystemStorageException;
}
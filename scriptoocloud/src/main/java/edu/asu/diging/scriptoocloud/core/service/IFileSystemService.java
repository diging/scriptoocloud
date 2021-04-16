package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * The interface which provides methods for adding and deleting files on the server file system.
 */
public interface IFileSystemService {

    /**
     * Getter for the RootLocationString or the root directory where users can add Directories and
     * Files
     *
     * @return The name of the root Directory from config.properties
     */
    String getRootLocationString();

    /**
     * Directories created on the system can be of the form: [root]/[username] or
     * [root]/[username]/[type]/[id]/[version].
     *
     * @param username The username to be used as a directory name.
     * @param type     The directory type (e.g., dataset, etc.)
     * @param id       The id to be used as a directory name.
     * @param version  The version (e.g., Dataset version)
     * @return true if the directory was successfully created, false otherwise.
     * @throws FileSystemStorageException The exception thrown if the directory could not be created
     *                                    due to an IOException, UnsupportedOperationException,
     *                                    or SecurityException.
     */
    boolean addDirectories(String username, String type, String id, String version)
            throws FileSystemStorageException;

    /**
     * A helper method to create a Path given a username and unique id.
     *
     * @param username The username of the owner of the files or directory (e.g. Dataset)
     *                 (username cannot be null).
     * @param type     The directory type (e.g., dataset, etc.) The type parameter can be null only
     *                 if the username is the only non-null parameter, resulting in a path of the form:
     *                 [root]/[username].
     * @param id       The id of the entity (e.g., Dataset) being stored.  The id parameter can be
     *                 null only if the username is the only non-null parameter, resulting in a path
     *                 of the form: [root]/[username].
     * @param version  The version of the entity (e.g., Dataset) being stored.  The version parameter
     *                 can be null if the username is the only non-null parameter, resulting in
     *                 a path of the form: [root]/[username] or, the version parameter can also be
     *                 null when e.g., deleting a Dataset. The resulting path:
     *                 [root]/[username]/[type]/[id] is used to delete the [id] directory, which includes
     *                 deleting the [version] directory therein, as well as any files the [version]
     *                 directory contains.
     * @return Returns a Path.
     * @throws InvalidPathException Exception if the path cannot be created.
     */
    Path createPath(String username, String type, String id, String version) throws
            FileSystemStorageException;

    /**
     * Deletes a directory on the filesystem.
     *
     * @param username The name (primary key) of the user / owner of the Dataset
     * @param type     The directory type (e.g., dataset, etc.)
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
     * [root]/[username]/[directory_type]/[directory_id]/[version]/[filename]
     *
     * @param username The name (primary key) of the user / owner of the type (e.g., Dataset) to
     *                 which the file belongs.
     * @param type     The directory type (e.g., dataset, etc.)
     * @param id       The id of the type to which the file belongs.
     * @param version  The version of the type to which the file belongs.
     * @param filename The name of the file to be stored.
     * @param bytes    The array of bytes making up the file.
     * @throws FileSystemStorageException If the file could not be stored on the file system.
     */
    void createFileInDirectory(String username, String type, String id, String version,
                               String filename, byte[] bytes) throws FileSystemStorageException;

    /**
     * Creates a zip file of the form:
     * [root]/[username]/[directory_type]/[directory_id]/[version]/[type]_[id]_version_[version].zip
     *
     * @param username The name (primary key) of the user / owner of the type (e.g., Dataset).
     * @param type     The directory type (e.g., dataset, etc.)
     * @param id       The id of the type
     * @param version  The version of the type
     * @throws FileSystemStorageException If the directory could not be downloaded as a zip file.
     * @throws IOException                If the streams cannot be closed.
     */
    void createZipFile(String username, String type, String id, String version)
            throws FileSystemStorageException, IOException;
}

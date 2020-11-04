package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Optional;

/**
 * The Service class associated with user Datasets.  The class handles creating directories on
 * on the file system and the association between Datasets and DataFiles they contain.
 */

@Service
public class DatasetService implements IDatasetService {

    private final String rootLocationString;
    private final DatasetRepository datasetRepository;
    private final DataFileRepository dataFileRepository;

    @Autowired
    public DatasetService(StorageProperties properties,
                          DatasetRepository datasetRepository,
                          DataFileRepository dataFileRepository) {
        this.rootLocationString = properties.getLocation();
        this.datasetRepository = datasetRepository;
        this.dataFileRepository = dataFileRepository;
    }

    /**
     * Creates the dataset in the filesystem, then in the database.
     *
     * @param name The name of the dataset.
     * @param user The IUser who owns the dataset
     * @throws DatasetException Exception thrown if dataset with same name already exists.
     */
    @Override
    public boolean createDataset(String name, IUser user) throws DatasetException {

        boolean success;
        // create directories on the file system
        try {
            success = addDatasetDirectories(name, user.getUsername());
        } catch (DatasetException e) {
            throw new DatasetException(
                    "An IOException prevented the Dataset from being created", e);
        }

        if (success) {
            // create directories in the database
            Optional<Dataset> existingDataset = datasetRepository.findByNameAndUser(name, user);
            if (existingDataset.isPresent()) {
                throw new DatasetException("User already has a dataset with this name");
            }
            Dataset dataset = new Dataset();
            dataset.setName(name);
            dataset.setUser(user);
            datasetRepository.save(dataset);
        }
        return success;
    }

    /**
     * Edits the dataset in the filesystem, then in the database.
     *
     * @param id       The id of the dataset to edit.
     * @param newName  The new name of the dataset to edit.
     * @param oldName  The old name of the dataset to edit.
     * @param username The username
     * @throws DatasetException If Dataset directories couldn't be edited.
     */
    @Override
    public boolean editDataset(Long id, String newName, String oldName, String username)
            throws DatasetException {
        boolean success;
        try {
            success = editDatasetDirectories(username, oldName, newName);
        } catch (SecurityException e){
            throw new DatasetException("A SecurityException prevented editing the Dataset", e);
        } catch (NullPointerException e) {
            throw new DatasetException("A NullPointer prevented editing the Dataset", e);
        }

        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (dataset.isPresent()) {
            dataset.get().setName(newName);
            datasetRepository.save(dataset.get());
        }
        return success;
    }

    /**
     * Deletes the dataset in the filesystem, then in the database.
     *
     * @param id The id of the dataset to delete.
     * @return Boolean indicating success.
     * @throws DatasetException If the Dataset couldn't be deleted
     */
    @Override
    public boolean deleteDataset(Long id) throws DatasetException {
        boolean success;
        // Delete Dataset Directories and files from the filesystem
        try {
            success = deleteDatasetDirectories(id);
        } catch (SecurityException e) {
            throw new DatasetException("Read access to directory was denied", e);
        }
        // Then delete Dataset and its files from the database
        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (dataset.isPresent()) {
            Set<DataFile> files = dataset.get().getFiles();
            for (DataFile file : files) {
                dataFileRepository.deleteById(file.getId());
            }
        }
        datasetRepository.deleteById(id);
        return success;
    }

    /**
     * Finds a dataset in the database given a particular id.
     *
     * @param id The id of the dataset.
     * @return Returns the IDataset.
     */
    @Override
    public IDataset findById(Long id) {
        Optional<Dataset> foundDataset = datasetRepository.findById(id);
        return foundDataset.orElse(null);
    }

    /**
     * Finds all datasets in the database (Likely for admin use).
     *
     * @return Returns a List of all IDatasets corresponding to Datasets in the database.
     */
    @Override
    public List<IDataset> findAll() {
        Iterable<Dataset> datasets = datasetRepository.findAll();
        List<IDataset> results = new ArrayList<>();
        datasets.iterator().forEachRemaining(results::add);
        return results;
    }

    /**
     * Finds all Datasets in the database for a particular user
     *
     * @param username The username of the owner of the database.
     * @return Returns a List of all IDatasets for a given user corresponding to Datasets in the database.
     */
    @Override
    public List<IDataset> findAllByUsername(String username) {
        Iterable<Dataset> datasets = datasetRepository.findAll();
        List<IDataset> results = new ArrayList<>();
        datasets.iterator().forEachRemaining(d -> {
            if (d.getUsername().equals(username)) {
                results.add(d);
            }
        });
        return results;
    }

    /**
     * Deletes a DataFile from a Dataset. This involves: 1. Removing the file from the appropriate
     * location on the file system and 2. Removing the file from the database ensuring that the
     * association with the parent Dataset is removed as well.
     *
     * @param datasetId The primary key of the Dataset
     * @param fileId    The primary key of the DataFile
     * @return boolean indicating success.
     * @throws DatasetException Exception detailing possible errors with the file and file system.
     */
    @Override
    public boolean deleteFileFromDataset(Long datasetId, Long fileId) throws DatasetException {
        boolean success = false;
        String username;
        String datasetName;
        String fileName;

        Optional<Dataset> dataset = datasetRepository.findById(datasetId);
        Optional<DataFile> dataFile = dataFileRepository.findById(fileId);
        if (dataset.isPresent() && dataFile.isPresent()) {
            username = dataset.get().getUsername();
            datasetName = dataset.get().getName();

            fileName = dataFile.get().getName();

            // Remove join from dataset
            dataset.get().getFiles().remove(dataFile.get());
            datasetRepository.save(dataset.get());

            // delete file from database
            dataFileRepository.deleteById(fileId);

            try {
                // Removes file from file system
                Path path = createPath(username, datasetName);
                String pathString = path.toString();
                path = Paths.get(pathString, fileName);
                File fileToBeDeleted = path.toFile();
                success = fileToBeDeleted.delete();
            } catch (InvalidPathException e) {
                throw new DatasetException("Path to file could not be found", e);
            } catch (UnsupportedOperationException e) {
                throw new DatasetException("Could not convert path to File", e);
            } catch (SecurityException e) {
                throw new DatasetException("File was created, but could not be deleted", e);
            }
        }
        return success;
    }

    /**
     * Creates the dataset (and its required directories) on the filesystem.
     *
     * @param name     The name of the dataset.
     * @param username The username of the dataset.
     * @throws DatasetException The exception thrown if the directory could not be created.
     */

    private boolean addDatasetDirectories(String name, String username) throws DatasetException {
        boolean success = false;
        // First, the correct directory must be created
        Path path = createPath(username, name);
        File directory = path.toFile();
        // if the Path was created successfully, create the directory
        try {
            if (!directory.exists()) {
                Files.createDirectories(path);
                success = true;
            }
        } catch (IOException e) {
            throw new DatasetException("IOException occurred in addDatasetDirectories", e);
        } catch (UnsupportedOperationException e) {
            throw new DatasetException("UnsupportedOperationException occurred in addDatasetDirectories", e);
        } catch (SecurityException e){
            throw new DatasetException("SecurityException occurred in addDatasetDirectories", e);
        }
        return success;
    }

    /**
     * Edits the dataset name on the filesystem.
     *
     * @param username The username of the owner of the dataset.
     * @param oldName  The old name of the dataset.
     * @param newName  The new name of the dataset.
     * @return Returns true if the edit was successful, false if it failed.
     */

    private boolean editDatasetDirectories(String username, String oldName, String newName) {
        boolean returnValue = false;
        Path path = createPath(username, oldName);
        File directory = path.toFile();
        try {
            if (directory.exists()) {
                Path newPath = createPath(username, newName);
                File newDirectory = newPath.toFile();
                returnValue = directory.renameTo(newDirectory);
            }
        } catch (SecurityException e) {
            throw new DatasetException("SecurityException occurred in editDatasetDirectories", e);
        } catch (NullPointerException e) {
            throw new DatasetException("NullPointerException occurred in editDatasetDirectories");
        }
        return returnValue;
    }

    /**
     * Deletes a dataset on the filesystem.
     *
     * @param id The id of the dataset.
     * @return Returns true if the delete was successful, false if it failed.
     * @throws SecurityException throws a storage exception if the directory could not be deleted.
     */

    private boolean deleteDatasetDirectories(Long id) throws SecurityException {
        boolean success = false;
        Optional<Dataset> dataset = datasetRepository.findById(id);
        String username;
        String name;
        if (dataset.isPresent()) {
            username = dataset.get().getUsername();
            name = dataset.get().getName();
            Path path = createPath(username, name);
            File directory = path.toFile();
            try {
                success = deleteDirectory(directory);
            } catch (SecurityException e) {
                throw new DatasetException("A SecurityException occurred in deleteDatasetDirectories", e);
            }
        }
        return success;
    }

    /**
     * Helper method to recursively delete directories.
     *
     * @param directoryToBeDeleted The directory / File to be deleted.
     * @return Returns true if the delete was successful, false if it failed.
     * @throws SecurityException if directory cannot be deleted.
     */
    private boolean deleteDirectory(File directoryToBeDeleted) throws DatasetException {
        boolean returnValue;
        File[] allContents;
        try {
            allContents = directoryToBeDeleted.listFiles();
        } catch (SecurityException e) {
            throw new DatasetException("A SecurityException occurred in deleteDirectory when generating file list", e);
        }
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        try {
            returnValue = directoryToBeDeleted.delete();
        } catch (SecurityException e) {
            throw new DatasetException("A SecurityException occurred in deleteDirectory when deleting a directory", e);
        }
        return returnValue;
    }

    /**
     * A helper method to create a Path given a username and dataset name.
     *
     * @param username    The username of the owner of the dataset.
     * @param datasetName The name of the dataset.
     * @return Returns a Path.
     */
    private Path createPath(String username, String datasetName) {
        Path path;
        if (datasetName != null) {
            try {
                path = Paths.get(this.rootLocationString, username, datasetName);
                String pathName = StringUtils.cleanPath(Objects.requireNonNull(path).toString());
                path = Paths.get(pathName);
                return path;
            } catch (InvalidPathException e) {
                throw new DatasetException("An InvalidPathException occurred in createPath with null datasetName", e);
            }
        } else {
            try {
                path = Paths.get(this.rootLocationString, username);
                String pathName = StringUtils.cleanPath(Objects.requireNonNull(path).toString());
                path = Paths.get(pathName);
            } catch (InvalidPathException e) {
                throw new DatasetException("An InvalidPathException occurred in createPath with datasetName present", e);
            }
            return path;
        }
    }
}

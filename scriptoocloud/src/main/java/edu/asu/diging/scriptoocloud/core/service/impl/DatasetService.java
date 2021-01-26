package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DataFileNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * The Service class associated with user Datasets.  The class handles the association between
 * Datasets and DataFiles they contain.
 */
@Transactional
@Service
public class DatasetService implements IDatasetService {

    private final FileSystemService fileSystemService;
    private final DatasetRepository datasetRepository;
    private final DataFileRepository dataFileRepository;

    @Autowired
    public DatasetService(FileSystemService fileSystemService,
                          DatasetRepository datasetRepository,
                          DataFileRepository dataFileRepository) {
        this.fileSystemService = fileSystemService;
        this.datasetRepository = datasetRepository;
        this.dataFileRepository = dataFileRepository;
    }

    /**
     * Creates the dataset in the filesystem, then in the database.
     *
     * @param name The name of the Dataset.
     * @param user The IUser who owns the dataset
     * @throws DatasetStorageException Exception thrown if dataset cannot be created.
     */
    @Override
    public void createDataset(String name, IUser user) throws DatasetStorageException {

        Dataset dataset = new Dataset();
        dataset.setName(name);
        dataset.setUser(user);
        Dataset savedDataset = datasetRepository.save(dataset);

        // create directories on the file system (using Dataset id)
        try {
            fileSystemService.addDatasetDirectories(String.valueOf(savedDataset.getId()),
                    user.getUsername());
        } catch (FileSystemStorageException e) {
            throw new DatasetStorageException("An IOException prevented the Dataset from being created", e);
        }
    }

    /**
     * Edits the dataset name in the database (Dataset path uses id).
     *
     * @param id      The id of the dataset to edit.
     * @param newName The new name of the dataset being edited.
     */
    @Override
    public void editDataset(Long id, String newName) {

        // filesystem path is: username/datasetId so no change is needed to the filesystem
        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (dataset.isPresent()) {
            dataset.get().setName(newName);
            datasetRepository.save(dataset.get());
        }
    }

    /**
     * Deletes the dataset in the filesystem, then in the database.
     *
     * @param id       The id of the dataset to delete.
     * @param username The username of the owner of the Dataset
     * @throws DatasetStorageException Exception detailing errors with the file and file system.
     */
    @Override
    public void deleteDataset(Long id, String username) throws DatasetStorageException {
        // Delete Dataset Directories and files from the filesystem
        try {
            fileSystemService.deleteDatasetDirectories(id, username);
        } catch (SecurityException e) {
            throw new DatasetStorageException("Read access to directory was denied", e);
        } catch (FileSystemStorageException e) {
            throw new DatasetStorageException("An invalid path prevented Dataset deletion", e);
        }
        // Then delete Dataset and its files from the database
        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (dataset.isPresent()) {
            dataFileRepository.deleteAllByDatasetId(id);
        }
        datasetRepository.deleteById(id);
    }

    /**
     * Finds a dataset in the database given a particular id.
     *
     * @param id The id of the dataset.
     * @return Returns the IDataset.
     */
    @Override
    public IDataset findById(Long id) {
        return datasetRepository.findById(id).orElse(null);
    }

    /**
     * Finds and returns a Page of Datasets for a User
     *
     * @param pageable The Pageable to calculate page size and current page
     * @param user     The user associated with the Dataset
     * @return The Page of Datasets
     */
    @Override
    public Page<Dataset> findDatasets(Pageable pageable, IUser user) {
        return datasetRepository.findAllByUser(user, pageable);
    }

    /**
     * Finds all datasets in the database (Likely for admin use).
     *
     * @return Returns Page lists of all Datasets in the database.
     */
    @Override
    public Page<Dataset> findAll(Pageable pageable) {
        return datasetRepository.findAll(pageable);
    }

    /**
     * Deletes a DataFile from a Dataset. This involves: 1. Removing the file from the appropriate
     * location on the file system and 2. Removing the file from the database ensuring that the
     * association with the parent Dataset is removed as well.
     *
     * @param datasetId The primary key of the Dataset
     * @param fileId    The primary key of the DataFile
     * @return boolean indicating success of deleting file
     * @throws DatasetStorageException Exception detailing errors with the file and file system.
     * @throws DatasetNotFoundException Exception if Dataset not found in the database.
     * @throws DataFileNotFoundException Exception if DataFile not found in the database.
     */
    @Override
    public boolean deleteFileFromDataset(Long datasetId, Long fileId) throws DatasetStorageException,
            DatasetNotFoundException, DataFileNotFoundException {

        Optional<Dataset> dataset = datasetRepository.findById(datasetId);
        Optional<DataFile> dataFile = dataFileRepository.findById(fileId);
        if (!dataset.isPresent()) {
            throw new DatasetNotFoundException("Dataset not found in the database");
        }
        if (!dataFile.isPresent()) {
            throw new DataFileNotFoundException("DataFile not found in the database");
        }
        String username = dataset.get().getUsername();
        String fileName = dataFile.get().getName();

        // Remove join from dataset
        dataset.get().getFiles().remove(dataFile.get());
        datasetRepository.save(dataset.get());

        // delete file from database
        dataFileRepository.deleteById(fileId);

        try {
            // Removes file from file system
            String pathString = fileSystemService.createPath(username,
                    String.valueOf(datasetId)).toString();
            File fileToBeDeleted = Paths.get(pathString, fileName).toFile();
            return fileSystemService.deleteDirectoryOrFile(fileToBeDeleted);
        } catch (FileSystemStorageException e) {
            throw new DatasetStorageException("Path to file could not be found", e);
        } catch (UnsupportedOperationException e) {
            throw new DatasetStorageException("Could not convert path to File", e);
        } catch (SecurityException e) {
            throw new DatasetStorageException("File was created, but could not be deleted", e);
        }
    }
}

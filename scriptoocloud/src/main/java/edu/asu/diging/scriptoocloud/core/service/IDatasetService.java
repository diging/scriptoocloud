package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DataFileNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface associated with user Datasets.  The interface handles the association between
 * Datasets and DataFiles they contain.
 */
public interface IDatasetService {

    /**
     * Creates the dataset in the filesystem, then in the database.
     *
     * @param name The name of the Dataset.
     * @param user The IUser who owns the dataset
     * @return The Dataset which was created.
     * @throws DatasetStorageException Exception thrown if dataset cannot be created.
     */
    Dataset createDataset(String name, IUser user) throws DatasetStorageException;

    /**
     * Edits the dataset name in the database (Dataset path uses id).
     *
     * @param id      The id of the dataset to edit.
     * @param newName The new name of the dataset being edited.
     */
    void editDataset(Long id, String newName);

    /**
     * Deletes the dataset in the filesystem, then in the database.
     *
     * @param id       The id of the dataset to delete.
     * @param username The username of the owner of the Dataset
     * @throws DatasetStorageException Exception detailing errors with the file and file system.
     */
    void deleteDataset(Long id, String username) throws DatasetStorageException;

    /**
     * Finds a dataset in the database given a particular id.
     *
     * @param id The id of the dataset.
     * @return Returns the IDataset.
     */
    IDataset findById(Long id);

    /**
     * Finds and returns a Page of Datasets for a User
     *
     * @param pageable The Pageable to calculate page size and current page
     * @param user     The user associated with the Dataset
     * @return The Page of Datasets
     */
    Page<Dataset> findDatasets(Pageable pageable, IUser user);

    /**
     * Finds all datasets in the database (Likely for admin use).
     *
     * @return Returns Page lists of all Datasets in the database.
     */
    Page<Dataset> findAll(Pageable pageable);

    /**
     * Deletes a DataFile from a Dataset. This involves: 1. Removing the file from the appropriate
     * location on the file system and 2. Removing the file from the database ensuring that the
     * association with the parent Dataset is removed as well.
     *
     * @param datasetId The primary key of the Dataset
     * @param fileId    The primary key of the DataFile
     * @return boolean indicating success of deleting file
     * @throws DatasetStorageException   Exception detailing errors with the file and file system.
     * @throws DatasetNotFoundException  Exception if Dataset not found in the database.
     * @throws DataFileNotFoundException Exception if DataFile not found in the database.
     */
    boolean deleteFileFromDataset(Long datasetId, Long fileId) throws DatasetStorageException,
            DatasetNotFoundException, DataFileNotFoundException;
}

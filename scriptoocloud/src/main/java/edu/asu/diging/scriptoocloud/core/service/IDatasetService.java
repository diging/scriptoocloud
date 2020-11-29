package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.data.domain.Page;

public interface IDatasetService {

    void createDataset(String name, IUser user) throws DatasetStorageException;

    void editDataset(Long id, String newName);

    void deleteDataset(Long id, String username) throws DatasetStorageException;

    IDataset findById(Long id);

    Page<Dataset> findAll();

    Page<Dataset> findAllByUser(IUser user);

    boolean deleteFileFromDataset(Long datasetId, Long fileId) throws DatasetStorageException;
}

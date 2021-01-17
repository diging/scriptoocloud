package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IDatasetService {

    void createDataset(String name, IUser user) throws DatasetStorageException;

    @PreAuthorize("hasPermission(#id, 'Dataset', 'edit')")
    void editDataset(Long id, String newName);

    @PreAuthorize("hasPermission(#id, 'Dataset', 'delete')")
    void deleteDataset(Long id, String username) throws DatasetStorageException;

    IDataset findById(Long id);

    Page<Dataset> findPaginatedDatasets(Pageable pageable, IUser user);

    Page<Dataset> findAll(Pageable pageable);

    @PreAuthorize("hasPermission(#fileId, 'DataFile', 'delete')")
    boolean deleteFileFromDataset(Long datasetId, Long fileId) throws DatasetStorageException;
}

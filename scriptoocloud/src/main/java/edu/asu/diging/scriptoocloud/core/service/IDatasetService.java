package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import java.util.List;

public interface IDatasetService {

    boolean createDataset(String name, IUser user) throws DatasetException;

    boolean editDataset(Long id, String newName, String oldName, String username) throws DatasetException;

    boolean deleteDataset(Long id) throws DatasetException ;

    IDataset findById(Long id);

    List<IDataset> findAll();

    List<IDataset> findAllByUsername(String username);

    boolean deleteFileFromDataset(Long datasetId, Long fileId) throws DatasetException;
}

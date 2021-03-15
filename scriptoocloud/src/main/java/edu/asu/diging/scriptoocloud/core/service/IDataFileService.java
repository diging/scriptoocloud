package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDataFileService {

    DataFile createFile(byte[] bytes, String datasetId, String username, String filename,
                        String type) throws DataFileStorageException;

    Page<DataFile> findFiles(Pageable pageable, Long datasetId);
}

package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDataFileService {

    void createFile(byte[] bytes, String datasetId, String username, String filename, String type);

    Page<DataFile> findPaginatedFiles(Pageable pageable, Long datasetId);
}

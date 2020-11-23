package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.domain.Page;

public interface IDataFileService {

    void createFile(byte[] bytes, String datasetId, String username, String filename, String type);

    Page<DataFile> getFilesByDatasetId(Long id);
}

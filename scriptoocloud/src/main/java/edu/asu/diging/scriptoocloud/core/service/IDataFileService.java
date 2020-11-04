package edu.asu.diging.scriptoocloud.core.service;

import org.springframework.web.multipart.MultipartFile;

public interface IDataFileService {

    boolean createFile(MultipartFile file, Long datasetId, String username, String datasetName);

    void editFile(Long id);
}

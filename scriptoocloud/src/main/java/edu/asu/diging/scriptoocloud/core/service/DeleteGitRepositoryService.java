package edu.asu.diging.scriptoocloud.core.service;

import java.io.File;

public interface DeleteGitRepositoryService {

    void deleteDirectoryContents(File file);
    
    void deleteRepository(Long id);
    
}
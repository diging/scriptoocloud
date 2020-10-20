package edu.asu.diging.scriptoocloud.core.service;

import java.io.File;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;

public interface DeleteGitRepositoryService {

    void deleteRepository(GitRepository gitRepository);
    
    void deleteDirectoryContents(File file);
    
}
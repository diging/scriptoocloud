package edu.asu.diging.scriptoocloud.core.service;

import java.io.File;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;

public interface DeleteGitRepositoryService {

    void deleteDirectoryContents(File file);

    void deleteRepository(GitRepository gitRepository);
    
}
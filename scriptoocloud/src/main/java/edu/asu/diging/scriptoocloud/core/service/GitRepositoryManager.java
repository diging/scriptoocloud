package edu.asu.diging.scriptoocloud.core.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.JGitInternalException;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;

public interface GitRepositoryManager {

    void deleteRepository(Long id) throws FileSystemStorageException;
    
    void cloneRepository(String gitUrl, String requester) throws InvalidGitUrlException, MalformedURLException, FileSystemStorageException, JGitInternalException, IOException, InterruptedException;

    ArrayList<GitRepository> listRepositories();

    String getRepositoryPath(Long id);

    Long getRepositoryImageId(Long id);

}
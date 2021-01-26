package edu.asu.diging.scriptoocloud.core.service;

import java.net.MalformedURLException;
import java.util.ArrayList;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;

public interface GitRepositoryManager {

    void deleteRepository(Long id) throws FileSystemStorageException;

    void cloneRepository(String gitUrl, String requester) throws InvalidGitUrlException, MalformedURLException, FileSystemStorageException;

    ArrayList<GitRepository> listRepositories();

}
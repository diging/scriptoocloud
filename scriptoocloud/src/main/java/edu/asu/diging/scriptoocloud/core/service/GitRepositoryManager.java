package edu.asu.diging.scriptoocloud.core.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.JGitInternalException;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;

public interface GitRepositoryManager {

    void deleteRepository(Long id);

    void cloneRepository(String gitUrl, String requester) throws InvalidGitUrlException, MalformedURLException, JGitInternalException, IOException, InterruptedException;

    ArrayList<GitRepository> listRepositories();

}
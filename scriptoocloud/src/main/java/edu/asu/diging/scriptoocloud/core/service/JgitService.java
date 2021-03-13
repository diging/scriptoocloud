package edu.asu.diging.scriptoocloud.core.service;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.jgit.api.errors.JGitInternalException;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;

public interface JgitService {

    void clone(String localRepoFolderName, String url) throws InvalidGitUrlException, JGitInternalException, IOException, InterruptedException;

}

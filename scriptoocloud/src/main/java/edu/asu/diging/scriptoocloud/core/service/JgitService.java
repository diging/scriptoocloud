package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;

public interface JgitService {

    void clone(String filename, String url) throws InvalidGitUrlException;

}

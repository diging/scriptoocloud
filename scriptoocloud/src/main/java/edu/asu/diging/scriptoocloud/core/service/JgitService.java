package edu.asu.diging.scriptoocloud.core.service;

import org.eclipse.jgit.api.errors.GitAPIException;

public interface JgitService {

    void clone(String filename, String url) throws GitAPIException;

}

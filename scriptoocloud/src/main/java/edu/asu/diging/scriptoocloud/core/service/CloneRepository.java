package edu.asu.diging.scriptoocloud.core.service;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.Repository;

public interface CloneRepository {

    ArrayList<Repository> getRepos();

    String cloneRepo(CloneForm cloneForm) throws GitAPIException;

    String deleteRepo(String url);

    /*
     * File.delete() only works on empty directories
     */
    void deleteDirectoryContents(File file);

    String deleteRepo(String url, String requester, String owner, String repo);

}
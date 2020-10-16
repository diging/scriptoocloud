package edu.asu.diging.scriptoocloud.core.service;

import java.io.File;
import java.util.ArrayList;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.Repository;

public interface CloneRepository {

    ArrayList<Repository> getRepos();

    String cloneRepo(CloneForm cloneForm);

    /*
     * File.delete() only works on empty directories
     */
    void deleteDirectoryContents(File file);

    String deleteRepo(String url, String requester, String owner, String repo);

}
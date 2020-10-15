package edu.asu.diging.scriptoocloud.core.service;

import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.Repository;

public interface CloneRepository {

    String cloneRepo(CloneForm cloneForm) throws GitAPIException;

    ArrayList<Repository> getRepos();

}
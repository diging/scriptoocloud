package edu.asu.diging.scriptoocloud.core.service;

import java.util.ArrayList;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface GitRepositoryManager {

    void deleteRepository(Long id);

    void cloneRepository(CloneForm cloneForm, IUser user);

    ArrayList<GitRepository> listRepositories();

}
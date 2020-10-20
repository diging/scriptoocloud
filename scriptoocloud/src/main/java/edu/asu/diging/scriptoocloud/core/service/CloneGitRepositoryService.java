package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface CloneGitRepositoryService {

    void cloneRepo(CloneForm cloneForm, IUser user);

}
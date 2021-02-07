package edu.asu.diging.scriptoocloud.core.service;

import java.util.List;

import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.service.impl.nullIDException;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface ProjectManager {

    Project createProject(String name, String description, IUser user);
    
    void deleteProject(long id) throws nullIDException;

    List<Project> findAll();

}
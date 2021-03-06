package edu.asu.diging.scriptoocloud.core.service;

import java.util.List;

import edu.asu.diging.scriptoocloud.core.exceptions.NullIDException;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface ProjectManager {

    Project createProject(String name, String description, IUser user);
    
    void deleteProject(Long id) throws NullIDException;

    List<Project> findAll();

}
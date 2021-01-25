package edu.asu.diging.scriptoocloud.core.service;

import java.util.List;

import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.service.impl.nullIDException;

public interface ProjectManager {

    Project createProject(String name, String description);
    
    void deleteProject(long id) throws nullIDException;

    List<Project> findAll();

}
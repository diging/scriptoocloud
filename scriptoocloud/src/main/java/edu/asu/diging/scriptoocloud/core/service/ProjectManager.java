package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.model.Project;

public interface ProjectManager {

    Project createProject(String name, String description);
    
    void deleteProject(String name);

}
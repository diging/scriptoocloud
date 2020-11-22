package edu.asu.diging.scriptoocloud.core.service;

import java.util.List;

import edu.asu.diging.scriptoocloud.core.model.Project;

public interface ProjectManager {

    Project createProject(String name, String description);
    
    Project getProject(long id);
    
    void deleteProject(long id);

	List<Project> findAll();

}
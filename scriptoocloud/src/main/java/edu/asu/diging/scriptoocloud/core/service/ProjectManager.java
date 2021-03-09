package edu.asu.diging.scriptoocloud.core.service;

import java.util.List;
import java.util.Optional;

import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface ProjectManager {

    Project createProject(String name, String description, IUser user);

    void deleteProject(Long id);

    List<Project> findAll();

	Optional<ProjectImpl> getProject(int id);

    void updateProject(int id, String name, String description);

}
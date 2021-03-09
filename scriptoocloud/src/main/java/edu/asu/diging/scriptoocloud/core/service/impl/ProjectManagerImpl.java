package edu.asu.diging.scriptoocloud.core.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.NestedServletException;

import edu.asu.diging.scriptoocloud.core.data.ProjectRepository;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;
import edu.asu.diging.simpleusers.core.model.IUser;

@Transactional
@Service
public class ProjectManagerImpl implements ProjectManager {

	@Autowired
	private ProjectRepository projectRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.scriptoocloud.core.service.impl.ProjectManager#createProject(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Project createProject(String name, String description, IUser user) {
		Project project = new ProjectImpl();
		project.setName(name);
		project.setUser(user);
		project.setDescription(description);
		project.setCreationDate(ZonedDateTime.now());
		project.setLastModifiedDate(ZonedDateTime.now());

		return projectRepo.save((ProjectImpl) project);
	}


    /*
     * (non-Javadoc)
     * 
     * deleteProject deletes a project based on a given project id. It is used in
     * the RemoveProjectController, listed as a button on existing projects. Null is
     * an illegal argument for this function.
     * 
     * @see
     * edu.asu.diging.scriptoocloud.core.service.impl.ProjectManager#deleteProject(
     * java.lang.String)
     */
    @Override
    public void deleteProject(Long id) {
        projectRepo.deleteById(id);
    }

	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public List<Project> findAll() {
		Iterable<ProjectImpl> projects = projectRepo.findAll();
		List<Project> results = new ArrayList<>();
		projects.forEach(e -> results.add((Project) e));
		return results;
	}

	@Override
	public void updateProject(int id, String name, String description) {
		//project.setId(id);
	    Optional<ProjectImpl> project = getProject(id);
	    Project project1 = project.get();
		project1.setName(name);
		project1.setDescription(description);
		project1.setLastModifiedDate(ZonedDateTime.now());

		projectRepo.save((ProjectImpl) project1);
	}


    @Override
    public Optional<ProjectImpl> getProject(int id) {
        Optional<ProjectImpl> project = Optional.ofNullable(new ProjectImpl());
        project = projectRepo.findById((long) id);
        return project;
    }

}

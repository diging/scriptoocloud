package edu.asu.diging.scriptoocloud.core.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger logger=LoggerFactory.getLogger(getClass());

    @Autowired
    private ProjectRepository projectRepo;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.scriptoocloud.core.service.impl.ProjectManager#createProject(java.lang.String, java.lang.String)
     */
	@Override
    public Project createProject(String name, String description, IUser user) {
        Project project = new ProjectImpl();
        project.setName(name);
        project.setUser(user);
        project.setDescription(description);
        project.setCreationDate(ZonedDateTime.now());
        
        return projectRepo.save((ProjectImpl)project);
    }
    
    /* (non-Javadoc)
     * 
     */
    @Override
    public Project getProject(long id) {
    	Project project = projectRepo.findById(id);
        
        return project;
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.scriptoocloud.core.service.impl.ProjectManager#deleteProject(java.lang.String)
     */
    @Override
    public void deleteProject(long id) {
   
        try {
        	// long is a primitive data type, so we must convert to an object data type Long to check for null. 
        	Long checkId = id;
        	if(checkId == null) {
        		throw new nullIDException("Project does not exist.");
        	} else {
        		projectRepo.deleteById(id);
        	}
        } catch (nullIDException err) {
        	logger.error("Project does not exist.");
        }
    }
    
    private Exception deleteNullIdException() {
		// TODO Auto-generated method stub
		return null;
	}

/* (non-Javadoc)
    * 
    */
   @Override
    public List<Project> findAll() {
        Iterable<ProjectImpl> projects = projectRepo.findAll();
        List<Project> results = new ArrayList<>();
        projects.forEach(e -> results.add((Project) e));
        return results;
    }
    
}

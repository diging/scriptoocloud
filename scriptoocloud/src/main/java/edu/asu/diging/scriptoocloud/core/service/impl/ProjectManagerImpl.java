package edu.asu.diging.scriptoocloud.core.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.data.ProjectRepository;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;

@Transactional
@Service
public class ProjectManagerImpl implements ProjectManager {

    @Autowired
    private ProjectRepository projectRepo;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.scriptoocloud.core.service.impl.ProjectManager#createProject(java.lang.String, java.lang.String)
     */
    @Override
    public Project createProject(String name, String description) {
        Project project = new ProjectImpl();
        project.setName(name);
        project.setDescription(description);
        project.setCreationDate(ZonedDateTime.now());
        
        return projectRepo.save((ProjectImpl)project);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.scriptoocloud.core.service.impl.ProjectManager#deleteProject(java.lang.String)
     */
    @Override
    public void deleteProject(long id) {
   
        try {
        	Project project = projectRepo.findById(id);
        	projectRepo.delete((ProjectImpl)project);
        } catch (NonUniqueResultException e) {
        }
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

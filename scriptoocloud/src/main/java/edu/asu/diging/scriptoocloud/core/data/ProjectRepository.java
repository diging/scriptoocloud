package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;

public interface ProjectRepository extends PagingAndSortingRepository<ProjectImpl, Long> {

    Project findByName(String name);
    
    Project findById(long id);
}

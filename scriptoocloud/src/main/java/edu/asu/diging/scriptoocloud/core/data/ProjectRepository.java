package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.repository.CrudRepository;

import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;

public interface ProjectRepository extends CrudRepository<ProjectImpl, Long> {

}

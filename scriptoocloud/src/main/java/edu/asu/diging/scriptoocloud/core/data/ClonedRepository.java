package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.scriptoocloud.core.model.Repository;
import edu.asu.diging.scriptoocloud.core.model.impl.RepositoryImpl;

public interface ClonedRepository extends PagingAndSortingRepository<Repository, Long> {

    public RepositoryImpl findByURI(String URI);
}

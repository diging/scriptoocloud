package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.scriptoocloud.core.model.Repository;
import edu.asu.diging.scriptoocloud.core.model.impl.RepositoryImpl;

public interface ClonedRepository extends PagingAndSortingRepository<RepositoryImpl, Long> {

    public RepositoryImpl findByRepo(String path);
    
    public RepositoryImpl findByPath(String path);
}

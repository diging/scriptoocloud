package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.scriptoocloud.core.model.impl.RepositoryImpl;

public interface ClonedRepository extends PagingAndSortingRepository<RepositoryImpl, Long> {

    public RepositoryImpl findByUrl(String url);
    
    public RepositoryImpl deleteByUrl(String url);

    public RepositoryImpl findByHost(String host);
    
    public RepositoryImpl findByOwner(String repo);
    
    public RepositoryImpl findByRepo(String owner);
    

    
}

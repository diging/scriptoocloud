package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;

public interface GitRepositoryRepository extends PagingAndSortingRepository<GitRepositoryImpl, Long> {

    public GitRepositoryImpl findByUrl(String url);
    
    public GitRepositoryImpl deleteByUrl(String url);

    public GitRepositoryImpl findBygitRepositoryHost(String host);
    
    public GitRepositoryImpl findBygitRepositoryOwner(String repo);
    
    public GitRepositoryImpl findBygitRepositoryName(String owner);
}

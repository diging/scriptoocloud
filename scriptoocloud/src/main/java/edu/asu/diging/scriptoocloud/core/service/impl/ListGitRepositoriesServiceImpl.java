package edu.asu.diging.scriptoocloud.core.service.impl;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.ListGitRepositoriesService;

@Transactional
@Service
public class ListGitRepositoriesServiceImpl implements ListGitRepositoriesService{

    @Autowired 
    private GitRepositoryRepository gitRepositoryJpa;

    @Override
    public ArrayList<GitRepository> getRepos(){
        Iterable<GitRepositoryImpl> repoModels = gitRepositoryJpa.findAll();
        ArrayList<GitRepository> reposList = new ArrayList<>();
        repoModels.iterator().forEachRemaining(r -> reposList.add(r));
        return reposList;
    }
}

package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.time.ZonedDateTime;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.CloneGitRepositoryService;
import edu.asu.diging.scriptoocloud.core.service.DeleteGitRepositoryService;
import edu.asu.diging.simpleusers.core.model.IUser;

@Transactional
@PropertySource("classpath:config.properties")
@Service
public class CloneGitRepositoryServiceImpl implements CloneGitRepositoryService {

    @Autowired
    JgitServiceImpl jGitService;

    @Autowired 
    private GitRepositoryRepository gitRepositoryJpa;
        
    @Autowired
    private DeleteGitRepositoryService deleteGitRepository;

    @Value("${git.repositories.path}")
    private String path;
    
    @Override
    public void cloneRepo(CloneForm cloneForm, IUser user){    
        
        String host = cloneForm.getHost();
        String owner = cloneForm.getOwner();
        String repositoryName = cloneForm.getRepo();
        String requester = user.getUsername();
        ZonedDateTime creationDate = ZonedDateTime.now();                                   
        String url = host + "/" + owner + "/" + repositoryName;
                                           
        GitRepositoryImpl gitRepository = new GitRepositoryImpl();
        gitRepository.setUrl(url);
        gitRepository.setGitRepositoryHost(host);
        gitRepository.setGitRepositoryOwner(owner);
        gitRepository.setGitRepositoryName(repositoryName);
        gitRepository.setRequester(requester);
        gitRepository.setCreationDate(creationDate);

        GitRepository repositoryEntity = gitRepositoryJpa.findByUrl(url);
        if(repositoryEntity != null){
            gitRepositoryJpa.deleteById(repositoryEntity.getId());
            deleteGitRepository.deleteRepository(gitRepository);
        }
        
        try{
            jGitService.clone(path + requester + "_" + owner + "_" + repositoryName, cloneForm.getUrl());
            gitRepositoryJpa.save(gitRepository);
        }
        catch(GitAPIException e){
            throw new IllegalArgumentException(e);
        }

    }

}

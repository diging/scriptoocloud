package edu.asu.diging.scriptoocloud.core.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.DeleteGitRepositoryService;
import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;
import edu.asu.diging.scriptoocloud.core.service.JgitService;
import edu.asu.diging.simpleusers.core.model.IUser;

@Service
@Transactional
@PropertySource("classpath:config.properties")
public class GitRepositoryService implements GitRepositoryManager{

    @Autowired
    private DeleteGitRepositoryService deleteGitRepositoryService;
    
    @Autowired 
    private GitRepositoryRepository gitRepositoryJpa;
    
    @Autowired
    private JgitService jGitService;

    @Value("${git.repositories.path}")
    private String path;
    
    @Override
    public void deleteRepository(Long id){
        deleteGitRepositoryService.deleteRepository(id);
    }
    
    @Override
    public void cloneRepository(CloneForm cloneForm, IUser user){
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
            deleteRepository(repositoryEntity.getId());
        }
        
        try{
            jGitService.clone(path + requester + "_" + owner + "_" + repositoryName, cloneForm.getUrl());
            gitRepositoryJpa.save(gitRepository);
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public ArrayList<GitRepository> listRepositories(){
        Iterable<GitRepositoryImpl> repoModels = gitRepositoryJpa.findAll();
        ArrayList<GitRepository> reposList = new ArrayList<>();
        repoModels.iterator().forEachRemaining(r -> reposList.add(r));
        return reposList;
    }
    
}

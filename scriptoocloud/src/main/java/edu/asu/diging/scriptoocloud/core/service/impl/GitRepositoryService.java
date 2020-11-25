package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;
import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;
import edu.asu.diging.scriptoocloud.core.service.JgitService;
import edu.asu.diging.scriptoocloud.core.service.UrlFormatterUtility;

@Service
@Transactional
@PropertySource("classpath:config.properties")
public class GitRepositoryService implements GitRepositoryManager{

    @Autowired
    private DeleteFilesService deleteFilesService;
    
    @Autowired
    private UrlFormatterUtility urlFormatter;
    
    @Autowired 
    private GitRepositoryRepository gitRepositoryJpa;
    
    @Autowired
    private JgitService jGitService;

    @Value("${git.repositories.path}")
    public String path;

    @Override
    public void cloneRepository(String gitUrl, String requester) throws InvalidGitUrlException, MalformedURLException{
        String folderName = urlFormatter.urlToFolderName(gitUrl);

        ZonedDateTime creationDate = ZonedDateTime.now();                                   
      
        GitRepositoryImpl gitRepository = new GitRepositoryImpl();
        gitRepository.setUrl(gitUrl);
        gitRepository.setRequester(requester);
        gitRepository.setCreationDate(creationDate);
        gitRepository.setFolderName(folderName);
        
        GitRepositoryImpl repositoryEntity = gitRepositoryJpa.findByUrl(gitUrl);
        
        if(repositoryEntity != null){
            repositoryEntity.setUrl(gitUrl);
            repositoryEntity.setRequester(requester);
            repositoryEntity.setCreationDate(creationDate);
            repositoryEntity.setFolderName(folderName);
            gitRepositoryJpa.save(repositoryEntity);
        }
        
        jGitService.clone(path + folderName, gitUrl);
        gitRepositoryJpa.save(gitRepository);
    }

    @Override
    public ArrayList<GitRepository> listRepositories(){
        Iterable<GitRepositoryImpl> repoModels = gitRepositoryJpa.findAll();
        ArrayList<GitRepository> reposList = new ArrayList<>();
        repoModels.iterator().forEachRemaining(r -> reposList.add(r));
        return reposList;
    }
        
    @Override
    public void deleteRepository(Long id){
        GitRepositoryImpl gitRepository = gitRepositoryJpa.findById(id).get();
        gitRepositoryJpa.deleteById(gitRepository.getId());
        File file = new File(path + gitRepository.getFolderName());
        deleteFilesService.deleteDirectoryContents(file);
    } 
    
}

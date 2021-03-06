package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.eclipse.jgit.api.errors.JGitInternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;
import edu.asu.diging.scriptoocloud.core.service.JgitService;
import edu.asu.diging.scriptoocloud.core.service.UrlFormatterUtility;

/*
 * Clones remote git repositories and facilitates transactional events related to git repositories in file system
 * 
 * @author Jason Ormsby
 */

@Service
@Transactional
@PropertySource("classpath:config.properties")
public class GitRepositoryService implements GitRepositoryManager {
    
    @Autowired
    private FileSystemService fileSystemService;
    
    @Autowired
    private UrlFormatterUtility urlFormatter;
    
    @Autowired 
    private GitRepositoryRepository gitRepositoryJpa;
    
    @Autowired
    private JgitService jGitService;
   
    @Value("${git.repositories.path}")
    public String path;
   
   /*
    * Checks system path property has trailing slash to facilitate concatenation with new file names
    * 
    */
    @PostConstruct
    private void validatePathProperty() {
        if(!path.endsWith("/")) {
            path += "/";
        }
    }
    
   /*
    * Handles requests to clone remote git repositories and stores an entity, or updates an entity, 
    * with information related to the request and repository
    * 
    * @param   gitUrl       non-malformed url of remote git repository 
    * @param   requester    username in current session that made the request
    */
    @Override
    public void cloneRepository(String gitUrl, String requester) throws InvalidGitUrlException, MalformedURLException {
        String folderName = urlFormatter.urlToFolderName(gitUrl);

        ZonedDateTime creationDate = ZonedDateTime.now();       
        
        GitRepositoryImpl repositoryEntity = gitRepositoryJpa.findByUrl(gitUrl);  
        if(repositoryEntity == null) {
            repositoryEntity = new GitRepositoryImpl();
        } else {
            fileSystemService.deleteDirectoryOrFile(new File(path + repositoryEntity.getFolderName()));
        }
        
        repositoryEntity.setUrl(gitUrl);
        repositoryEntity.setRequester(requester);
        repositoryEntity.setCreationDate(creationDate);
        repositoryEntity.setFolderName(folderName);
  
        //need to remove current image if doing an update
  
        try {
            
            //why optional??
            Optional<String> imageId = jGitService.clone(path + folderName, gitUrl);
            
            if(imageId.isPresent())
              repositoryEntity.setImageId(imageId.get());
            else
              repositoryEntity.setImageId("no id");

        //are we making custom exceptions for docker imagebuild exceptions?
    
        } catch (JGitInternalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidGitUrlException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gitRepositoryJpa.save(repositoryEntity);
    }
    
   /*
    * Lets you view metadata of all git repositories on the file system
    * 
    * @return A list of GitRepository objects in database
    */
    @Override
    public ArrayList<GitRepository> listRepositories() {
        Iterable<GitRepositoryImpl> repoModels = gitRepositoryJpa.findAll();
        ArrayList<GitRepository> reposList = new ArrayList<>();
        repoModels.iterator().forEachRemaining(r -> reposList.add(r));
        return reposList;
    }
 
    /*
    * Removes git repository from file system and erases metadata from database related to it
    * @param    id  id of specific entity in sequential id column to be deleted
    *     
    */
    @Override
    public void deleteRepository(Long id) {
        GitRepositoryImpl gitRepository = gitRepositoryJpa.findById(id).get();
        gitRepositoryJpa.deleteById(gitRepository.getId());
        File file = new File(path + gitRepository.getFolderName());
        fileSystemService.deleteDirectoryOrFile(file);
    } 
    
}

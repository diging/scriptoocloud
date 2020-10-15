package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.asu.diging.scriptoocloud.core.data.ClonedRepository;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.Repository;
import edu.asu.diging.scriptoocloud.core.model.impl.RepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.CloneRepository;
import edu.asu.diging.simpleusers.core.model.IUser;

@Transactional
@Service
public class CloneRepositoryImpl implements CloneRepository{

    @Autowired 
    private ClonedRepository clonedRepository;
    
    private String host;
    private String owner;
    private String repo;
    private String requester;
    private String url;
    private ZonedDateTime creationDate;
    private String drive = "C:/";
    
    @Override
    public ArrayList<Repository> getRepos(){
        Iterable<RepositoryImpl> repoModels = clonedRepository.findAll();
        ArrayList<Repository> reposList = new ArrayList<>();
        repoModels.iterator().forEachRemaining(r -> reposList.add(r));
        return reposList;
    }

    @Override
    public String cloneRepo(CloneForm cloneForm) throws GitAPIException {    
        
        host = cloneForm.getHost();
        owner = cloneForm.getOwner();
        repo = cloneForm.getRepo();
        requester = ((IUser)SecurityContextHolder.getContext()
                            .getAuthentication().getPrincipal()).getUsername();
        creationDate = ZonedDateTime.now();                                   
        url = host + "/" + owner + "/" +repo;
                                           
        RepositoryImpl repository = new RepositoryImpl();
        repository.setUrl(url);
        repository.setHost(host);
        repository.setOwner(owner);
        repository.setRepo(repo);
        repository.setRequester(requester);
        repository.setCreationDate(creationDate);
        
        try{
            Repository repositoryEntity = clonedRepository.findByUrl(url);
            if(repositoryEntity != null){
                clonedRepository.deleteById(repositoryEntity.getId());
                File parentDirectory = new File(drive + requester + "_" + owner + "_" + repo);
                deleteDirectoryContents(parentDirectory);
            }
        }
        catch(InvalidDataAccessResourceUsageException e){}
        
        Git.cloneRepository().setURI(cloneForm.getUrl())
        .setDirectory(new File(drive + requester + "_" + owner + "_" + repo))
        .call().getRepository().close();

        clonedRepository.save(repository);
        
        return "github/clone";
    }
    
    
    
    
    
    @Override
    public String deleteRepo(String url, String requester, String owner, String repo){
    
        try{
            Repository repositoryEntity = clonedRepository.findByUrl(url);
            if(repositoryEntity != null){
                clonedRepository.deleteById(repositoryEntity.getId());
                File parentDirectory = new File(drive + requester + "_" + owner + "_" + repo);
                deleteDirectoryContents(parentDirectory);
            }
        }
        catch(InvalidDataAccessResourceUsageException e){}
    
        return "github/clone";
    }
    
    
    
    

    /*
     * File.delete() only works on empty directories
     */
    @Override
    public void deleteDirectoryContents(File file){
        
        File directorycontents[];
        
        if(file.isDirectory())
        {
            directorycontents = file.listFiles();
            for(File fileItem : directorycontents)
            {
                deleteDirectoryContents(fileItem);     
            }
              
            file.delete();
        }
        else
        {
            file.delete();
        }
    }

}

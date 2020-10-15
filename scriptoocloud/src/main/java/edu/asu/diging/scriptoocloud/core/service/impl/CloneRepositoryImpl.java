package edu.asu.diging.scriptoocloud.core.service.impl;

    import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
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
    
    private Scanner scanner;
    private String host;
    private String owner;
    private String repo;
    private String requester;
    private String url;
    private ZonedDateTime creationDate;
    private String dir = "C:/testRepo";
    
    
    @Override
    public ArrayList<Repository> getRepos(){
        Iterable<RepositoryImpl> repoModels = clonedRepository.findAll();
        ArrayList<Repository> reposList = new ArrayList<>();
        repoModels.iterator().forEachRemaining(r -> reposList.add(r));
        return reposList;
    }

    @Override
    public String cloneRepo(CloneForm cloneForm) throws GitAPIException {    
        url = cloneForm.getUrl();
        host = cloneForm.getHost();
        owner = cloneForm.getOwner();
        repo = cloneForm.getRepo();
        requester = ((IUser)SecurityContextHolder.getContext()
                            .getAuthentication().getPrincipal()).getUsername();
        creationDate = ZonedDateTime.now();                                   
                                   
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
                System.out.println("\n\nDELETING");
                File parentDirectory = new File(dir + requester + owner + repo);
                deleteDirectoryContents(parentDirectory);
            }
        }
        catch(InvalidDataAccessResourceUsageException e){}
        
        Git.cloneRepository().setURI(url)
        .setDirectory(new File(dir + requester + owner + repo)).call().getRepository().close();

        clonedRepository.save(repository);
        
        return "github/clone";
    }

/*
 * Java can only delete empty directories
 */
public void deleteDirectoryContents(File file){
    
    File directorycontents[];
    
    if(file.isDirectory())
    {
        directorycontents = file.listFiles();
        for(File fileItem : directorycontents){
            deleteDirectoryContents(fileItem);
                System.out.print(fileItem);        
        }
          
        file.delete();
    }
    else{
        file.delete();
    }
}

}

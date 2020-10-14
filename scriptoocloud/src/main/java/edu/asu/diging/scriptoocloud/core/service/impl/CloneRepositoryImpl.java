package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.asu.diging.scriptoocloud.core.data.ClonedRepository;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
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
    private String path;
    private String requester;
    private ZonedDateTime creationDate;
    private String dir = "C:/testRepo";
    
    
    @Override
    public String cloneRepo(CloneForm cloneForm) throws InvalidRemoteException, 
        TransportException, GitAPIException, MalformedURLException, NoSuchElementException {    

        URL repoURL = new URL(cloneForm.url);     
        String fullPath = repoURL.getPath();
        scanner = new Scanner(fullPath).useDelimiter("/");
        
        host = repoURL.getHost();
        owner = scanner.next();
        repo = scanner.next();
        while(scanner.hasNext())
            path += scanner.next();
            
        requester = ((IUser)SecurityContextHolder.getContext()
                            .getAuthentication().getPrincipal()).getUsername();
                             
        creationDate = ZonedDateTime.now();
                                   

        RepositoryImpl repository = new RepositoryImpl();
        repository.setHost(host);
        repository.setOwner(owner);
        repository.setRepo(repo);
        repository.setPath(path);
        repository.setRequester(requester);
        repository.setCreationDate(creationDate);
        
        if(clonedRepository.findByPath(path) != null){
              return "redirect:clone" + "?repoAlreadyExists";   
        }
        
        
        //new file is making a new dir every time
        //find out how to open a dir file into memory
        //Git.cloneRepository().setURI(url).setDirectory(new File(dir)).call();
        
   
        
        clonedRepository.save(repository);
        
        return "redirect:clone" + "?success";
    }
    


    
}

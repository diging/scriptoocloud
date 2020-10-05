package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.asu.diging.scriptoocloud.core.data.RepoRepository;
import edu.asu.diging.scriptoocloud.core.model.Repository;
import edu.asu.diging.scriptoocloud.core.service.CloneRepositoryService;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;

@Transactional
@Service
public class CloneRepositoryImpl implements CloneRepositoryService  {

    @Autowired 
    private RepoRepository repoRepository;
    
    private String owner;
    private String repo;
    private String branch;
    private String version;
    private String requester;
    private String dir = "C:/testRepo";
    
    public String cloneRepo(String url) throws InvalidRemoteException, 
        TransportException, GitAPIException, MalformedURLException {    
       
        requester = ((IUser)SecurityContextHolder.getContext()
                            .getAuthentication().getPrincipal()).getUsername();
              
        URL repoURL = new URL(url);
       
        if(!repoURL.getHost().equals("github.com"))
        {
            return "redirect:clone" + "?badHost";
        }
       
        String fullPath = repoURL.getPath().substring(1);
        String[] path = fullPath.split("/");
        
        Repository repository = new Repository();
        
        
        switch(path.length)
        {
            
            case 2:
                owner = path[0]; 
                repo = path[1]; 
                repository.setOwner(owner);
                repository.setRepo(repo);
                repository.setRequester(requester);
                repository.setBranch("");
                repository.setVersion("");
                dir += "_" + owner + "_" + repo + "_" + requester;
                break;
            
            case 3:
                owner = path[0]; 
                repo = path[1]; 
                branch = path[2];
                repository.setOwner(owner);
                repository.setRepo(repo);
                repository.setRequester(requester);
                repository.setBranch(branch);
                repository.setVersion("");      
                dir += "_" + owner + "_" + repo + "_" + requester
                            +branch;
                break;
                
            case 4:
                owner = path[0]; 
                repo = path[1]; 
                branch = path[2];
                version = path[3];
                repository.setOwner(owner);
                repository.setRepo(repo);
                repository.setRequester(requester);
                repository.setBranch(branch);
                repository.setVersion(version);    
                dir += "_" + owner + "_" + repo + "_" + requester
                            + branch + "_" + version;
                break;               
        
            default:
             return "redirect:clone" + "?badURL";   
        }

        repoRepository.save(repository);
        Git git = Git.cloneRepository().setURI(url).setDirectory(new File(dir)).call();
        
        return "redirect:clone" + "?success";
    }
    

    @Override
    public String getCloneDirectory() {
        return dir;
    }

    @Override
    public void setCloneDirectory(String path) {
        dir = path;
    }


    
}

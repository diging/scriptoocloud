package edu.asu.diging.scriptoocloud.core.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.scriptoocloud.core.data.RepoRepository;

@Entity
public class Repository {

    @Id
    private String owner;
    private String repo;
    private String branch;
    private String version;
    private String requester;
    private String dir = "C:/testRepo";
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String getRepo() {
        return repo;
    }
    
    public void setRepo(String repo) {
        this.repo = repo;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getRequester() {
        return requester;
    }
    
    public void setRequester(String requester) {
        this.requester = requester;
    }
    
    public String getDir() {
        return dir;
    }
    
    public void setDir(String dir) {
        this.dir = dir;
    }

}   

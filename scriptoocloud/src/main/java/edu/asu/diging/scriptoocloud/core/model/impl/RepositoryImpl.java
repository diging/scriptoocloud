package edu.asu.diging.scriptoocloud.core.model.impl;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.NotBlank;

import edu.asu.diging.scriptoocloud.core.model.Repository;

@Entity
public class RepositoryImpl implements Repository {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private long id;
    
    @NotEmpty(message = "Host name required")
    private String host;

    @NotEmpty(message = "Repository owner and name required")
    private String owner;
    
    @NotEmpty(message = "Repository name required")
    private String repo;
    
    private String path;
    private ZonedDateTime creationDate;
    private String Requester;
    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getHost() {
        return host;
    }
    
    @Override
    public void setHost(String host) {
        this.host = host;
    }
    
    @Override
    public String getOwner() {
        return owner;
    }
    
    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    @Override
    public String getRepo() {
        return repo;
    }
    
    @Override
    public void setRepo(String repo) {
        this.repo = repo;
    }
    
    @Override
    public String getPath() {
        return path;
    }
    
    @Override
    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
    
    @Override
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
    @Override
    public String getRequester() {
        return Requester;
    }
    
    @Override
    public void setRequester(String requester) {
        Requester = requester;
    }
    
}   

package edu.asu.diging.scriptoocloud.core.model.impl;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;

@Entity
public class GitRepositoryImpl implements GitRepository {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private long id;
    
    @NotEmpty(message = "Host name required")
    private String gitRepositoryHost;

    @NotEmpty(message = "Repository owner and name required")
    private String  gitRepositoryOwner;
    
    @NotEmpty(message = "Repository name required")
    private String gitRepositoryName;
    
    private String url;
    private ZonedDateTime creationDate;
    private String requester;
    
     @Override
    public long getId() {
        return id;
    }
    @Override
    public void setId(long id) {
        this.id = id;
    }
    @Override
    public String getGitRepositoryHost() {
        return gitRepositoryHost;
    }
    @Override
    public void setGitRepositoryHost(String gitRepositoryHost) {
        this.gitRepositoryHost = gitRepositoryHost;
    }
    @Override
    public String getGitRepositoryOwner() {
        return gitRepositoryOwner;
    }
    @Override
    public void setGitRepositoryOwner(String gitRepositoryOwner) {
        this.gitRepositoryOwner = gitRepositoryOwner;
    }
    @Override
    public String getGitRepositoryName() {
        return gitRepositoryName;
    }
    @Override
    public void setGitRepositoryName(String gitRepositoryName) {
        this.gitRepositoryName = gitRepositoryName;
    }
    @Override
    public String getUrl() {
        return url;
    }
    @Override
    public void setUrl(String url) {
        this.url = url;
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
        return requester;
    }
    @Override
    public void setRequester(String requester) {
        this.requester = requester;
    }
    
}   

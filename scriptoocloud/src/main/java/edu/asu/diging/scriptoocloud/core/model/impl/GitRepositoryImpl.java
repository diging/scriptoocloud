package edu.asu.diging.scriptoocloud.core.model.impl;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;

@Entity
public class GitRepositoryImpl implements GitRepository {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private long id;
    private String url;
    private ZonedDateTime creationDate;
    private String requester;
    private String folderName;
    
    @Override
    public String getFolderName() {
        return folderName;
    }
    @Override
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    @Override
    public long getId() {
        return id;
    }
    @Override
    public void setId(long id) {
        this.id = id;
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

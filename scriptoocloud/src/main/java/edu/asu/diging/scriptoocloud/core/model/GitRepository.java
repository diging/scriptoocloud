package edu.asu.diging.scriptoocloud.core.model;

import java.time.ZonedDateTime;

public interface GitRepository {

    long getId();

    void setId(long id);

    String getGitRepositoryHost();

    void setGitRepositoryHost(String gitRepositoryHost);

    String getGitRepositoryOwner();

    void setGitRepositoryOwner(String gitRepositoryOwner);

    String getGitRepositoryName();

    void setGitRepositoryName(String gitRepositoryName);

    String getUrl();

    void setUrl(String url);

    ZonedDateTime getCreationDate();

    void setCreationDate(ZonedDateTime creationDate);

    String getRequester();

    void setRequester(String requester);

}
package edu.asu.diging.scriptoocloud.core.model;

import java.time.ZonedDateTime;

public interface Repository {

    String getOwner();

    void setOwner(String owner);

    String getRepo();

    void setRepo(String repo);

    String getUrl();

    void setUrl(String url);

    ZonedDateTime getCreationDate();

    void setCreationDate(ZonedDateTime creationDate);

    String getRequester();

    void setRequester(String requester);

    String getHost();

    void setHost(String host);

    long getId();

    void setId(long id);

}
package edu.asu.diging.scriptoocloud.core.model;

import java.time.ZonedDateTime;

public interface Repository {

    String getOwner();

    void setOwner(String owner);

    String getRepo();

    void setRepo(String repo);

    String getPath();

    void setPath(String path);

    ZonedDateTime getCreationDate();

    void setCreationDate(ZonedDateTime creationDate);

    String getRequester();

    void setRequester(String requester);

    String getHost();

    void setHost(String host);

}
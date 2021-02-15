package edu.asu.diging.scriptoocloud.core.model;

import java.time.ZonedDateTime;

public interface GitRepository {

    long getId();

    void setId(long id);

    String getUrl();

    void setUrl(String url);

    ZonedDateTime getCreationDate();

    void setCreationDate(ZonedDateTime creationDate);

    String getRequester();

    void setRequester(String requester);

    String getFolderName();

    void setFolderName(String folderName);

    String getImageId();

    void setImageId(String imageId);

}
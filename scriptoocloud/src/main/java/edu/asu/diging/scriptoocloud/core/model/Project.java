package edu.asu.diging.scriptoocloud.core.model;

import java.time.ZonedDateTime;

import edu.asu.diging.simpleusers.core.model.IUser;

public interface Project {

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    ZonedDateTime getCreationDate();

    void setCreationDate(ZonedDateTime creationDate);
    
    ZonedDateTime getLastModifiedDate();

    void setLastModifiedDate(ZonedDateTime lastModifiedDate);

    void setUser(IUser user);

    String getUsername();

}
package edu.asu.diging.scriptoocloud.core.model;

import java.time.ZonedDateTime;

public interface Project {

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    ZonedDateTime getCreationDate();

    void setCreationDate(ZonedDateTime creationDate);

}
package edu.asu.diging.scriptoocloud.core.model;

import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.simpleusers.core.model.IUser;

import java.time.ZonedDateTime;
import java.util.Set;

public interface IDataset {

    void setId(Long id);

    Long getId();

    void setCreationDate(ZonedDateTime creationDate);

    ZonedDateTime getCreationDate();

    void setName(String name);

    String getName();

    void setVersion(Long version);

    Long getVersion();

    void setUser(IUser user);

    String getUsername();

    Set<DataFile> getFiles();

    Long addFile(DataFile dataFile);

    void setDescription(String description);

    String getDescription();
}

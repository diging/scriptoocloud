package edu.asu.diging.scriptoocloud.core.model;

import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.simpleusers.core.model.IUser;

import java.util.Set;

public interface IDataset {

    void setId(Long id);

    Long getId();

    String getName();

    void setName(String name);

    void setUser(IUser user);

    String getUsername();

    Set<DataFile> getFiles();

    Long addFile(DataFile dataFile);
}

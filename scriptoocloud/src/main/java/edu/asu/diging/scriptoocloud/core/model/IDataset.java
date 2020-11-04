package edu.asu.diging.scriptoocloud.core.model;

import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface IDataset {

    void setId(Long id);

    Long getId();

    String getName();

    void setName(String name);

    void setUser(IUser user);

    String getUsername();

    void addFile(DataFile dataFile);

    void removeFile(DataFile dataFile);

}

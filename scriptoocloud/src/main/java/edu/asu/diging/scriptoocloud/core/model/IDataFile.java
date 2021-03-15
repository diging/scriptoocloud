package edu.asu.diging.scriptoocloud.core.model;

import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;

import java.time.OffsetDateTime;

public interface IDataFile {

    void setId(Long id);

    Long getId();

    void setName(String name);

    String getName();

    void setExtension(String type);

    String getExtension();

    void setDataset(Dataset dataset);

    IDataset getDataset();

    void setCreatedAt(OffsetDateTime createdAt);

    OffsetDateTime getCreatedAt();

    String getOwner();
}

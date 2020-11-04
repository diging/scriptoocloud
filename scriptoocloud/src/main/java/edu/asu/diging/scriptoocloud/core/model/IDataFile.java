package edu.asu.diging.scriptoocloud.core.model;

public interface IDataFile {

    void setId(Long id);

    Long getId();

    void setName(String name);

    String getName();

    void setType(String type);

    String getType();

    void setDataset(IDataset dataset);

    IDataset getDataset();

}

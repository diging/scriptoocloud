package edu.asu.diging.scriptoocloud.web.forms;

import javax.validation.constraints.NotEmpty;

public class DatasetEditForm {

    private Long id;
    @NotEmpty(message = "Dataset name cannot be empty")
    private String newName;
    private Long newVersion;
    private String newDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public Long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }
}

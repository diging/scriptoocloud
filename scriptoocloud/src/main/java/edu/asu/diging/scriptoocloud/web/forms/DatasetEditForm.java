package edu.asu.diging.scriptoocloud.web.forms;

import javax.validation.constraints.NotEmpty;

public class DatasetEditForm {

    private Long id;
    private int index;
    @NotEmpty(message = "Dataset name cannot be empty")
    private String newName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}

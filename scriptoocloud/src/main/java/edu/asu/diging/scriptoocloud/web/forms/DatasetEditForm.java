package edu.asu.diging.scriptoocloud.web.forms;

import edu.asu.diging.scriptoocloud.web.validation.FieldNotSame;

import javax.validation.constraints.NotEmpty;

@FieldNotSame(first = "oldName", second = "newName", message = "New Dataset name is the same as the old Dataset name")
public class DatasetEditForm {

    private Long id;
    private int index;
    private String oldName;
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

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}

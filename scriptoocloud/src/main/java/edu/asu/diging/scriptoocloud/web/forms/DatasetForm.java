package edu.asu.diging.scriptoocloud.web.forms;

import javax.validation.constraints.NotEmpty;

public class DatasetForm {
    private Long id;
    @NotEmpty(message="Please enter a name for this Dataset.")
    private String name;
    private String username;
    private Long version;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion(){
        return version;
    }

    public void setVersion(Long version){
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

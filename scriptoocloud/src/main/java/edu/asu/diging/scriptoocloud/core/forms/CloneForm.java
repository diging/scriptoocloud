package edu.asu.diging.scriptoocloud.core.forms;

import javax.validation.constraints.NotEmpty;

public class CloneForm {

    @NotEmpty(message = "URL required") 
    public String url;

    @NotEmpty(message = "Host name required")
    private String host;

    @NotEmpty(message = "Repository owner and name required")
    private String owner;
    
    @NotEmpty(message = "Repository name required")
    private String repo;
   



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}

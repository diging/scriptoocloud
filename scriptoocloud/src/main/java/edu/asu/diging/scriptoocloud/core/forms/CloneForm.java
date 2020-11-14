package edu.asu.diging.scriptoocloud.core.forms;

import javax.validation.constraints.NotEmpty;

import edu.asu.diging.scriptoocloud.web.validation.RepositoryUrlConstraint;

@RepositoryUrlConstraint(url = "url")
public class CloneForm {
 
    @NotEmpty(message = "URL required") 
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

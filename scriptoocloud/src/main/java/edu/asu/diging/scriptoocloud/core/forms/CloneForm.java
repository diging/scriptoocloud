package edu.asu.diging.scriptoocloud.core.forms;

import javax.validation.constraints.NotEmpty;

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

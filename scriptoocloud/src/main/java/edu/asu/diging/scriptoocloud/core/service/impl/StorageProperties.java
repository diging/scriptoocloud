package edu.asu.diging.scriptoocloud.core.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/config.properties")
public class StorageProperties {

    @Value("${rootUploadLocation}")
    private String rootLocationString;

    public String getLocation() {
        return rootLocationString;
    }

    public void setLocation(String rootLocationString) {
        this.rootLocationString = rootLocationString;
    }

}

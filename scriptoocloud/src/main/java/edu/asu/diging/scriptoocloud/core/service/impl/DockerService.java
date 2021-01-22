package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Response;

@Service
@PropertySource("classpath:config.properties")
public class DockerService extends DockerRestConnection {

    @Value("${docker.dockerfile.path}")
    private String defaultDockerfilePath;
    
    public String buildImage(String projectName) throws FileNotFoundException, InterruptedException{
        File file = new File(defaultDockerfilePath);
        String response = dockerClient.buildImageCmd(new FileInputStream(file)).start().awaitImageId();
        return response;
    }
   
    public DockerService buildContainer(String projectName) throws IOException{
    
        String jsonBody = "{\"Image\":\"" + projectName + "\"}";
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonBody.getBytes());
    
        Request request = Request.builder()
        .putHeader("Content-Type", "application/json").method(Request.Method.POST)
                             .path("/containers/create")
                             .body(bis)
                             .build();
                             
        Response response = dockerRequest(request);
        
        return this;
    }
    
    public DockerService runContainer(String projectName) throws IOException{
        return this;
    }
    
}

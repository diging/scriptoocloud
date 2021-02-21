package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Response;

@Service
@PropertySource("classpath:config.properties")
public class DockerService {

    @Value("${docker.dockerfile.path}")
    private String defaultDockerfilePath;
    
    @Autowired
    private DockerRestConnection dockerRestConnection;
    
    public String buildImage(String dockerFileLocation) throws FileNotFoundException, InterruptedException{
        File file = new File(dockerFileLocation + ".tar");
        String response = dockerRestConnection.dockerClient.buildImageCmd(new FileInputStream(file)).start().awaitImageId();
        return response;
    }
   
    public DockerService buildContainer(String imageId, String[] userArgs) throws IOException{
    
        String jsonBody = "{"
            +"\"Image\":" + "\"" + imageId + "\","
            +"\"Cmd\": [" + "\"" + "test.py" + "\"" +"]"
            +"}";
                                
                                System.out.println(jsonBody);
        
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonBody.getBytes());
    
        Request request = Request.builder()
        .putHeader("Content-Type", "application/json").method(Request.Method.POST)
                             .path("/containers/create")
                             .body(bis)
                             .build();
                             
        String containerId = dockerRestConnection.dockerClient.createContainerCmd(imageId).withCmd(userArgs).exec().getId();

       //START CONTAINER 
       dockerRestConnection.dockerClient.startContainerCmd(containerId).exec();

        return this;
    }
    
    public DockerService runContainer(String projectName) throws IOException{
        return this;
    }
    
}

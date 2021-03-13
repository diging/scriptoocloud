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

@Service
@PropertySource("classpath:config.properties")
public class DockerService {

    @Value("${docker.dockerfile.path}")
    private String defaultDockerfilePath;
    
    @Autowired
    private DockerRestConnection dockerRestConnection;
    
    public String buildImage(String dockerFileLocation) throws FileNotFoundException, InterruptedException{
        File file = new File(dockerFileLocation);
        String response = dockerRestConnection.dockerClient.buildImageCmd(new FileInputStream(file)).start().awaitImageId();
        return response;
    }
   
    public void buildContainer(String imageId, String[] userArgs) throws IOException{   
                             
        String containerId = dockerRestConnection.dockerClient.createContainerCmd(imageId).withCmd(userArgs).exec().getId();

        dockerRestConnection.dockerClient.startContainerCmd(containerId).exec();
    }
    
    public String runContainer(String projectName) throws IOException{
        return "";
    }
    
}

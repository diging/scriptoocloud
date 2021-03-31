package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.model.Volume;

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
   
    public String buildContainer(String imageId, List<String> arguments) throws IOException{   
               
        String containerId = dockerRestConnection.dockerClient.createContainerCmd(imageId).withCmd("22").exec().getId();
        dockerRestConnection.dockerClient.startContainerCmd(containerId).exec();
        //InputStream stream = dockerRestConnection.dockerClient.copyArchiveFromContainerCmd(containerId, "out.txt").exec();
      
        return ""; 
    }
    
    public String runContainer(String containerId) throws IOException{
        
        
  

       /*results
        dockerRestConnection.dockerClient.logContainerCmd(containerId).toString();
        dockerRestConnection.dockerClient.copyArchiveFromContainerCmd(containerId, resource);
       */
       
        return "";
    }
    
}

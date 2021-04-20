package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.model.Volume;
import com.google.common.io.Files;

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
   
    public String buildContainer(String imageId, String[] arguments) throws IOException{
        String containerId =  dockerRestConnection.dockerClient.createContainerCmd(imageId).withCmd(arguments).exec().getId();
        return containerId; 
    }
    
    public String runContainer(String containerId, String outputFileName) throws IOException{
        dockerRestConnection.dockerClient.startContainerCmd(containerId).exec();   
        InputStream stream = dockerRestConnection.dockerClient
                                .copyArchiveFromContainerCmd(containerId, "/usr/src/app/" + outputFileName).exec();
        byte[] outputBytes = IOUtils.toByteArray(stream);
        //this dataset needs to be tied to a user before its written
        //this dataset needs to be written to the dataset folder
        FileOutputStream fos = new FileOutputStream("C:/"+outputFileName);
        fos.write(outputBytes);
        fos.close(); 
        return "";
    }
    
}

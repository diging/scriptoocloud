package edu.asu.diging.scriptoocloud.core.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Response;

@Service
@PropertySource("classpath:config.properties")
public class DockerRestConnection {

    private DockerClientConfig dockerConfig;
    private DockerHttpClient dockerHttpClient;
    protected DockerClient dockerClient;

    @Value("${docker.engine.host}")
    private String host;
    
    @Value("${docker.tls}")
    private String dockerTls;
    
    @Value("${git.repositories.path}")
    private String path;
    
    @PostConstruct
    private void init(){  
        dockerConfig = DefaultDockerClientConfig
                        .createDefaultConfigBuilder()
                        .withDockerHost(host)
                        .withDockerTlsVerify(dockerTls)
                        .build(); 
        
        dockerHttpClient = new ApacheDockerHttpClient
                        .Builder()
                        .dockerHost(dockerConfig.getDockerHost())
                        .sslConfig(dockerConfig.getSSLConfig())
                        .build(); 
                        
                        
        dockerClient = DockerClientImpl.getInstance(dockerConfig, dockerHttpClient);
    }
    
    public Response dockerRequest(Request request){
        return dockerHttpClient.execute(request);
    }
    
}

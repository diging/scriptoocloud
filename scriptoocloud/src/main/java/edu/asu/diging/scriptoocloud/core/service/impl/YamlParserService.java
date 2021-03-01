package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.service.JgitService;


/*
 * Take in primary key for a repository and parse the yaml file 
 * Return necessary data to controller so yaml arguments can be presented to user
*/

@Service
class YamlParserService  {


    public Map<String,Object> parseYaml(String filePathString) throws FileNotFoundException{  
        File file = new File(filePathString);
        return parseYaml(file);
    }


    public Map<String,Object> parseYaml(File file) throws FileNotFoundException{  
        if(file.isDirectory()){ 
            File[] fileTree = file.listFiles();
            for( File childFile : fileTree ){
                if(childFile.getPath().contains("yaml")){
                    file = childFile;
                    break;
                }
            }  
        }
        return parseYamlFile(file);
    }

    private Map<String,Object> parseYamlFile(File file) throws FileNotFoundException{
        
        if(file.isDirectory()){ 
            File[] fileTree = file.listFiles();
            for( File childFile : fileTree ){
                if(childFile.getPath().contains("yaml")){
                    file = childFile;
                    break;
                }
            }  
        }
        
        Yaml snakeYaml = new Yaml();
       
        Map<String, Object> yamlObject = snakeYaml.load(new FileInputStream(file));

        return yamlObject;
    }
    
     
}
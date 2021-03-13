package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

@Service
class YamlParserService  {

    public Map<String,Object> parseYaml(File dir) throws FileNotFoundException{  
            File[] fileTree = dir.listFiles();
            for( File childFile : fileTree ){
                if(childFile.getPath().contains("STC.yaml")){
                    dir = childFile;
                    break;
                }
            }  
        return parseYamlFile(dir);
    }

    private Map<String,Object> parseYamlFile(File file) throws FileNotFoundException{
        if(file.isDirectory()){ 
            File[] fileTree = file.listFiles();
            for( File childFile : fileTree ){
                if(childFile.getPath().endsWith(".yaml")){
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
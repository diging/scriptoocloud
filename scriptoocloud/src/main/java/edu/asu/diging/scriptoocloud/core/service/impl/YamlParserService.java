package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;


/*
*
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
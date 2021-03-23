package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import edu.asu.diging.scriptoocloud.core.model.impl.YamlModel;
import edu.asu.diging.scriptoocloud.core.service.IYamlParserService;

@Service
class YamlParserService implements IYamlParserService  {

    @Override
    public YamlModel parseYaml(File dir) throws FileNotFoundException{  
            File[] fileTree = dir.listFiles();
            for( File childFile : fileTree ){
                if(childFile.getPath().contains("STC.yaml")){
                    dir = childFile;
                    break;
                }
            }  
            
        Map<String, Object> stcYamlKeyPair = parseYamlFile(dir);
        
        YamlModel model = new YamlModel();
        model.setAuthor((String)stcYamlKeyPair.get("author"));
        model.setDescription((String)stcYamlKeyPair.get("description"));
        model.setMain((String)stcYamlKeyPair.get("main"));
        model.setName((String)stcYamlKeyPair.get("name"));
        model.setOutputContext((String)stcYamlKeyPair.get("output"));
        model.setInputParams((String[])stcYamlKeyPair.get("input"));
 
        return model;
    }

    @Override
    public Map<String,Object> parseYamlFile(File file) throws FileNotFoundException{
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
package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import edu.asu.diging.scriptoocloud.core.model.impl.YamlModel;
import edu.asu.diging.scriptoocloud.core.service.IYamlParserService;

@Service
class YamlParserService implements IYamlParserService  {


    @Value("${git.repositories.path}")
    public String path;

    @Override
    public YamlModel parseYamlInDirectory(String dirStringPath) throws FileNotFoundException{   
        //what should the default stc yaml file be named?
        File file = new File(dirStringPath + "/test.yml");
        
        Map<String, Object> stcYamlKeyPair = parseYamlFile(file);
        
        YamlModel model = new YamlModel();
        
        model.setAuthor((String)stcYamlKeyPair.get("author"));
        model.setDescription((String)stcYamlKeyPair.get("description"));
        model.setMain((String)stcYamlKeyPair.get("main"));
        model.setExtension((String)stcYamlKeyPair.get("extension"));
        model.setName((String)stcYamlKeyPair.get("name"));
        model.setOutputContext((String)stcYamlKeyPair.get("output"));
        model.setInputParams((ArrayList<String>)stcYamlKeyPair.get("input"));
 
        return model;
    }

    @Override
    public Map<String,Object> parseYamlFile(File file) throws FileNotFoundException{
        //maybe use a single method rather then two
        Yaml snakeYaml = new Yaml();
        Map<String, Object> yamlObject = snakeYaml.load(new FileInputStream(file));
        return yamlObject;
    }
}
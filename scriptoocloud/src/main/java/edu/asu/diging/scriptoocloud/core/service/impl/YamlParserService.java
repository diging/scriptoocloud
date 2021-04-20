package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

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
        File file = new File(dirStringPath + "/STC.yml");
        
        Map<String, Object> stcYamlKeyPair = parseYamlFile(file);
        
        YamlModel model = new YamlModel();
        
        //need checked excpetionf or incorrect yaml file
        
        model.setAuthor((String)stcYamlKeyPair.get("author"));
        model.setDescription((String)stcYamlKeyPair.get("description"));
        model.setMain(((String) stcYamlKeyPair.get("main")).split("\\.")[0]);
        model.setExtension(((String) stcYamlKeyPair.get("main")).split("\\.")[1]);
        model.setName((String)stcYamlKeyPair.get("name"));
        model.setOutputContext(((String)stcYamlKeyPair.get("output")).split("\\.")[0]);
        model.setOutputExtension(((String)stcYamlKeyPair.get("output")).split("\\.")[1]);
        model.setInputParams((ArrayList<String>)stcYamlKeyPair.get("input"));
       
        return model;
    }

    @Override
    public Map<String,Object> parseYamlFile(File file) throws FileNotFoundException{
        Map<String, Object> yamlObject = (new Yaml()).load(new FileInputStream(file));
        return yamlObject;
    }
}
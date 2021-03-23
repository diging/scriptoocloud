package edu.asu.diging.scriptoocloud.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import edu.asu.diging.scriptoocloud.core.model.impl.YamlModel;

public interface IYamlParserService {

    public YamlModel parseYamlInDirectory(String dir) throws FileNotFoundException;

    public Map<String, Object> parseYamlFile(File file) throws FileNotFoundException;

}
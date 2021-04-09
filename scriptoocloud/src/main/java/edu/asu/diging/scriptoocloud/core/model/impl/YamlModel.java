package edu.asu.diging.scriptoocloud.core.model.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;

@Entity

public class YamlModel  {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private long id;
    private String name;
    private String description;
    private String author;
    private String main;
    private String extension;
    private ArrayList<String> inputParams;
    private String outputContext;
  
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }
    public List getInputParams() {
        return inputParams;
    }
    public void setInputParams(ArrayList<String> inputParams) {
        this.inputParams = inputParams;
    }
    public String getOutputContext() {
        return outputContext;
    }
    public void setOutputContext(String outputContext) {
        this.outputContext = outputContext;
    }
 
}   

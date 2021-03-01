package edu.asu.diging.scriptoocloud.core.model.impl;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;

//do we need entity tag?
@Entity

//this class should directly model the yaml files we are retrieving
public class YamlModel  {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private long id;
    private String name;
    private String description;
    private String author;
    private String main;
    private String[] inputParams;
    private String outputContext;
    
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
    public String[] getInputParams() {
        return inputParams;
    }
    public void setInputParams(String[] inputParams) {
        this.inputParams = inputParams;
    }
    public String getOutputContext() {
        return outputContext;
    }
    public void setOutputContext(String outputContext) {
        this.outputContext = outputContext;
    }
 
}   

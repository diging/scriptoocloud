package edu.asu.diging.scriptoocloud.core.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public interface CloneRepositoryService {

    String cloneRepo(String url) throws InvalidRemoteException, 
        TransportException, GitAPIException, MalformedURLException;

    String getCloneDirectory();

    void setCloneDirectory(String path);


}   

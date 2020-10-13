package edu.asu.diging.scriptoocloud.core.service;

import java.net.MalformedURLException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public interface CloneRepository {

    String cloneRepo(String url)
            throws InvalidRemoteException, TransportException, GitAPIException, MalformedURLException;

}
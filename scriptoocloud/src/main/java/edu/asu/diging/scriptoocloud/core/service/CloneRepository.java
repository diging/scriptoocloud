package edu.asu.diging.scriptoocloud.core.service;

import java.net.MalformedURLException;
import java.util.NoSuchElementException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;

public interface CloneRepository {

    String cloneRepo(CloneForm cloneForm) throws InvalidRemoteException, TransportException, GitAPIException,
            MalformedURLException, NoSuchElementException;

}
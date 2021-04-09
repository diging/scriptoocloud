package edu.asu.diging.scriptoocloud.core.service;

import java.net.MalformedURLException;
import java.util.ArrayList;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;

/**
 * The interface which provides methods for cloning, listing, and deleting Git repositories.
 */
public interface GitRepositoryManager {

    /**
     * Handles requests to clone remote git repositories and stores an entity, or updates an entity,
     * with information related to the request and repository
     *
     * @param gitUrl    non-malformed url of remote git repository
     * @param requester username in current session that made the request
     */
    void cloneRepository(String gitUrl, String requester) throws InvalidGitUrlException, MalformedURLException, FileSystemStorageException;

    /**
     * Lets you view metadata of all git repositories on the file system
     *
     * @return A list of GitRepository objects in database
     */
    ArrayList<GitRepository> listRepositories();

    /**
     * Removes git repository from file system and erases metadata from database related to it
     *
     * @param id id of specific entity in sequential id column to be deleted
     */
    void deleteRepository(Long id) throws FileSystemStorageException;


}
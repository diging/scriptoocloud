package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import org.eclipse.jgit.api.errors.JGitInternalException;

/**
 * An interface which provides method(s) for cloning a remote Git repository.
 */
public interface JgitService {

    /**
     * Creates folder in file system and clones a remote git repository to it
     *
     * @param localRepoFolderName Name for new folder that will store cloned remote git repository
     * @param remoteGitRepoUrl    nonmalformed Url passed by user that points to remote git repository
     * @throws InvalidGitUrlException If remote repository doesn't exist or jgit encounters failure
     *                                related to copying remote repository at url
     * @throws JGitInternalException  JGit command execution failure at low level
     */
    void clone(String localRepoFolderName, String remoteGitRepoUrl) throws InvalidGitUrlException,
            FileSystemStorageException;

}

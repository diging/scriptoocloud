package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.service.JgitService;


/**
 * A Service class which clones remote git repositories to file system utilizing the JGit dependency
 *
 * @author Jason Ormsby
 */

@Service
class JgitServiceImpl implements JgitService {

    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public void clone(String localRepoFolderName, String remoteGitRepoUrl) throws InvalidGitUrlException, JGitInternalException, FileSystemStorageException {
        try {
            Git.cloneRepository().setURI(remoteGitRepoUrl).setDirectory(new File(localRepoFolderName)).call().close();
        } catch (GitAPIException e) {
            fileSystemService.deleteDirectoryOrFile(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        }
    }

}
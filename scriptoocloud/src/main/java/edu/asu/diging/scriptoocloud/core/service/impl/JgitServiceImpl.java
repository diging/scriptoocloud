package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.service.JgitService;


/*
 * Clones remote git repositories to file system utilizing the JGit dependency
 * 
 * @author Jason Ormsby
*/

@Service
class JgitServiceImpl implements JgitService {

    @Autowired
    private FileSystemService fileSystemService;

    /*
     * Creates folder in file system and clones a remote git repository to it
     * 
     * @param   localRepoFolderName         Name for new folder that will store cloned remote git repository 
     * @param   remoteGitRepoUrl            nonmalformed Url passed by user that points to remote git repository
     * @throws  InvalidGitUrlException      If remote repository doesn't exist 
     *                                      or jgit encounters failure related to copying remote repository at url
     * @throws  JGitInternalException       JGit command execution failure at low level                                       
    */
    @Override
    public void clone(String localRepoFolderName, String remoteGitRepoUrl) throws InvalidGitUrlException, JGitInternalException {
        try {
            Git.cloneRepository().setURI(remoteGitRepoUrl).setDirectory(new File(localRepoFolderName)).call().close();
        } catch(GitAPIException e) {
            fileSystemService.deleteDirectoryOrFile(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        }
        
        //Place a tar of the repository inside the root for docker
        try {
            Git git = Git.open(new File(localRepoFolderName));
            FileOutputStream out = new FileOutputStream("docker.tar");
            Repository db = git.getRepository();
            git.archive().setTree(db.resolve("HEAD")).setOutputStream(out).call();
        } catch (IOException e) {
            fileSystemService.deleteDirectoryOrFile(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        } catch (GitAPIException e) {
            fileSystemService.deleteDirectoryOrFile(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        }
        
    }
}
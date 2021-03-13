package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.asu.diging.scriptoocloud.core.data.YamlModelRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.impl.YamlModel;
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
    public void clone(String localRepoFolderName, String remoteGitRepoUrl) throws InvalidGitUrlException, JGitInternalException, IOException, InterruptedException {
        try {
            Git.cloneRepository().setURI(remoteGitRepoUrl).setDirectory(new File(localRepoFolderName)).call().close();
            new TarWriter(localRepoFolderName).writeDir(new File(localRepoFolderName));
        } catch(GitAPIException e) {
            fileSystemService.deleteDirectoryOrFile(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        }
    }
    
}
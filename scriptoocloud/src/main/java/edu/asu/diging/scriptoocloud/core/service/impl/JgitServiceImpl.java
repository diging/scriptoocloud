package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;
import edu.asu.diging.scriptoocloud.core.service.JgitService;

@Service
class JgitServiceImpl implements JgitService{

    @Autowired
    private DeleteFilesService deleteFilesService;

    @Override
    public void clone(String localRepoFolderName, String remoteGitRepoUrl) throws InvalidGitUrlException{
        try{
            Git.cloneRepository().setURI(remoteGitRepoUrl).setDirectory(new File(localRepoFolderName)).call().close();
        } catch(GitAPIException e){
            deleteFilesService.deleteDirectoryContents(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        } catch(JGitInternalException e){
            deleteFilesService.deleteDirectoryContents(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        }
    }

}
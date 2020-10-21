package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.DeleteGitRepositoryService;
import edu.asu.diging.scriptoocloud.core.service.JgitService;


@Service
class JgitServiceImpl implements JgitService{

    @Autowired
    DeleteGitRepositoryService deleteGitRepositoryService;

    @Override
    public void clone(String filename, String url){
        try{
            Git.cloneRepository().setURI(url)
            .setDirectory(new File(filename))
            .call().getRepository().close();
        }
        catch(GitAPIException e){
            deleteGitRepositoryService.deleteDirectoryContents(new File(filename));
            throw new IllegalArgumentException(e);
        }
    }

}
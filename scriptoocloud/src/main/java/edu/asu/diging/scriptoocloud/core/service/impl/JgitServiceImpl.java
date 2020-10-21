package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.JgitService;


@Service
class JgitServiceImpl implements JgitService{

    @Override
    public void clone(String filename, String url) throws GitAPIException {
            Git.cloneRepository().setURI(url)
            .setDirectory(new File(filename))
            .call().getRepository().close();
    }

}
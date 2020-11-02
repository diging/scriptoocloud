package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jgit.api.ArchiveCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.archive.TarFormat;
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
    public void clone(String filename, String url) throws InvalidGitUrlException{
        try{
            Git.cloneRepository().setURI(url)
            .setDirectory(new File(filename))
            .call().getRepository().close();
            
            Git git = Git.open(new File(filename));
            FileOutputStream out = new FileOutputStream("Docker.tar");
            Repository db = git.getRepository();

            git.archive().setTree(db.resolve("HEAD")).setOutputStream(out) .call();
            
        }catch(GitAPIException e){
            deleteFilesService.deleteDirectoryContents(new File(filename));
            throw new InvalidGitUrlException(e);
        }
        catch(JGitInternalException e){
            deleteFilesService.deleteDirectoryContents(new File(filename));
            throw new InvalidGitUrlException(e);
        } catch (IOException e) {
            deleteFilesService.deleteDirectoryContents(new File(filename));
            throw new InvalidGitUrlException(e);
        }
        
    }

}
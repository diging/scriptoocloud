package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.DeleteGitRepositoryService;
import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;

@Transactional
@PropertySource("classpath:config.properties")
@Service
public class DeleteGitRepositoryServiceImpl implements DeleteGitRepositoryService{

    @Autowired 
    private GitRepositoryRepository gitRepositoryJpa;

    @Value("${git.repositories.path}")
    private String path;

    @Override
    public void deleteRepository(Long id){
        GitRepositoryImpl gitRepository = gitRepositoryJpa.findById(id).get();
        gitRepositoryJpa.deleteById(gitRepository.getId());
        File file = new File(path + gitRepository.getRequester() + "_" + gitRepository.getGitRepositoryOwner() 
                                + "_" + gitRepository.getGitRepositoryName());
        deleteDirectoryContents(file);
    }
    
    @Override
    public void deleteDirectoryContents(File file){
        File directorycontents[];   
        if(file.isDirectory()){
            directorycontents = file.listFiles();
            for(File fileItem : directorycontents){
                deleteDirectoryContents(fileItem);     
            }
            file.delete();
        }
        else{
            file.delete();
        }
    }    
 
}

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
    
    @Autowired
    private DockerService dockerService;
    
    @Autowired
    YamlParserService yamlParserService;
    
    @Autowired
    YamlModelRepository yamlRepositoryJpa;
    

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
    public Optional<String> clone(String localRepoFolderName, String remoteGitRepoUrl) throws InvalidGitUrlException, JGitInternalException, IOException {
        try {
            Git.cloneRepository().setURI(remoteGitRepoUrl).setDirectory(new File(localRepoFolderName)).call().close();
            
            
        /*
          THESE METHODS ARE FOR TESTING, 
          when file upload and project class tickets are complete move to respective classes                           
         */
            //parse yaml create model
            Map<String,Object> yamlMap = yamlParserService.parseYaml(new File(localRepoFolderName));
            System.out.println(yamlMap);
            
            //create new yaml model and store with associated project for future updates from user
              
            YamlModel yamlModel = yamlRepositoryJpa.findByName("");  
            if(yamlModel == null) {
                yamlModel = new YamlModel();
                yamlModel.setName((String)yamlMap.get("name"));
                yamlModel.setAuthor((String)yamlMap.get("author"));
                yamlModel.setInputParams((String[])yamlMap.get("params"));
                yamlModel.setMain((String)yamlMap.get("main"));
                yamlModel.setOutputContext((String)yamlMap.get("output"));
               
            } 
            //pack tar of repo
            new TarWriter(localRepoFolderName).writeDir(new File(localRepoFolderName));
            
            //make docker image
            String imageId = dockerService.buildImage(localRepoFolderName);
            
            //build container
            String[] test = {"test.py"};
            dockerService.buildContainer(imageId,test);
            
            return Optional.of(imageId);
         /*
            THESE METHODS ARE FOR TESTING                           
         */
            
            
            
        } catch(GitAPIException e) {
            fileSystemService.deleteDirectoryOrFile(new File(localRepoFolderName));
            throw new InvalidGitUrlException(e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return null;
    }
    
}
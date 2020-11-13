package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;

class JgitServiceImplTest {

    @Mock
    private DeleteFilesService deleteFilesService;

    @InjectMocks
    private JgitServiceImpl serviceToTest;
    
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);  
    }
    
    @Test
    public void test_clone_success(){

        String folderName = "clone_success";
        File file = new File(folderName);
        
        Assertions.assertDoesNotThrow(()->serviceToTest.clone(folderName, "https://github.com/diging/scriptoocloud"));
      
        Assertions.assertTrue(file.exists());
        
        DeleteFilesServiceImpl deleteFilesService = new DeleteFilesServiceImpl();
        deleteFilesService.deleteDirectoryContents(file);
    }
    
    @Test
    public void test_clone_noSuchRepository(){
        String folderName = "no_such_repository";
        File file = new File(folderName);

        Assertions.assertThrows(InvalidGitUrlException.class,
            ()->serviceToTest.clone(folderName, "github.com/test"));

        Mockito.verify(deleteFilesService).deleteDirectoryContents(Mockito.any());
        Assertions.assertTrue(file.exists());  
        DeleteFilesServiceImpl deleteFilesService = new DeleteFilesServiceImpl();
        deleteFilesService.deleteDirectoryContents(file);  
        Assertions.assertFalse(file.exists());                 
    }   
    
    @Test
    public void test_clone_badUrl(){
        String folderName = "bad_url";
        File file = new File(folderName);

        Assertions.assertThrows(InvalidGitUrlException.class,
            ()->serviceToTest.clone(folderName, "githubcom/test"));

        Mockito.verify(deleteFilesService).deleteDirectoryContents(Mockito.any());
        Assertions.assertTrue(file.exists());  
        DeleteFilesServiceImpl deleteFilesService = new DeleteFilesServiceImpl();
        deleteFilesService.deleteDirectoryContents(file);  
        Assertions.assertFalse(file.exists());      
    } 

}

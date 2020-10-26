package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.MalformedURLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;

class JgitServiceImplTest {

    @InjectMocks
    private JgitServiceImpl serviceToTest;
    
    @Mock
    private DeleteFilesService deleteFilesService;
    
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);  
    } 
    
    @Test
    public void test_clone_noSuchRepository(){
        Assertions.assertThrows(InvalidGitUrlException.class,
            ()->serviceToTest.clone("testFile", "github.com/test"));
    }   
    
    @Test
    public void test_clone_badUrl(){
        Assertions.assertThrows(InvalidGitUrlException.class,
            ()->serviceToTest.clone("testFile", "githubm/test"));
    }   
}

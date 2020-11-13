package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;
import edu.asu.diging.scriptoocloud.core.service.JgitService;
import edu.asu.diging.scriptoocloud.core.service.UrlFormatterUtility;

class GitRepositoryManagerServiceTest {

    @Mock
    private JgitService jGitService;
    
    @Mock
    private GitRepositoryRepository gitRepositoryJpa;
    
    @Mock
    private DeleteFilesService deleteFilesService;
    
    @Mock
    private UrlFormatterUtility urlFormatter;
    
    @Mock
    private GitRepositoryImpl gitRepository;
    
    @InjectMocks
    private GitRepositoryService serviceToTest;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);   
    }
    
    @Test
    public void test_cloneRepository_clone() throws MalformedURLException, InvalidGitUrlException {
        Mockito.when(urlFormatter.urlToFolderName("https://github.com/diging/scriptoocloud"))
            .thenReturn("github.com_diging_scriptoocloud");
        Mockito.when(gitRepositoryJpa.findByUrl("github.com_diging_scriptoocloud")).thenReturn(null);
        
        Assertions.assertDoesNotThrow(
            ()->serviceToTest.cloneRepository("https://github.com/diging/scriptoocloud", "admin"));  
            
        GitRepositoryImpl repo  = new GitRepositoryImpl();
        Mockito.when(gitRepositoryJpa.save(repo)).thenReturn(repo);
        
        Mockito.verify(jGitService).clone(Mockito.any(),Mockito.any());
        Mockito.verify(gitRepositoryJpa).save(Mockito.any());
    }
        
    @Test
    public void test_listRepositories_returnList() {
        ArrayList<GitRepository> gitRepositoryList = new ArrayList<GitRepository>();
        GitRepositoryImpl gitRepository = new GitRepositoryImpl();
        gitRepositoryList.add(gitRepository);
        Iterable iterable = (Iterable) gitRepositoryList;
        
        Mockito.when(gitRepositoryJpa.findAll()).thenReturn(iterable);
        
        Assertions.assertEquals(iterable, serviceToTest.listRepositories());
    }   
        
    @Test
    public void test_deleteRepository_delete() {
        GitRepositoryImpl gitRepositoryImpl =  new GitRepositoryImpl();
        gitRepositoryImpl.setFolderName("test");
        
        Mockito.when(gitRepositoryJpa.findById((long) 0)).thenReturn(Optional.of(gitRepositoryImpl));
        Assertions.assertDoesNotThrow(()->serviceToTest.deleteRepository((long) 0));
        
        Mockito.verify(gitRepositoryJpa).deleteById(Mockito.any());
        Mockito.verify(deleteFilesService).deleteDirectoryContents(Mockito.any());
    }   
}

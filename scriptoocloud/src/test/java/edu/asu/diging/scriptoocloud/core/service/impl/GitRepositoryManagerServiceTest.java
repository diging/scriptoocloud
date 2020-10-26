package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
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
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;
import edu.asu.diging.scriptoocloud.core.service.JgitService;
import edu.asu.diging.scriptoocloud.core.service.UrlFormatterService;

class GitRepositoryManagerServiceTest {

    @InjectMocks
    private GitRepositoryService serviceToTest;
    
    @Mock
    private JgitService jGitService;
    
    @Mock
    private GitRepositoryRepository gitRepositoryJpa;
    
    @Mock
    private DeleteFilesService deleteFilesService;
    
    @Mock
    private UrlFormatterService urlFormatter;
    
    @Mock
    private GitRepositoryImpl gitRepository;
    
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);   
    }
    
    @Test
    public void test_cloneRepository_clone() throws MalformedURLException {
        Mockito.when(urlFormatter.urlToFolderName("https://github.com/diging/scriptoocloud"))
            .thenReturn("github.com_diging_scriptoocloud");
        Mockito.when(gitRepositoryJpa.findByUrl("github.com_diging_scriptoocloud")).thenReturn(null);
        Assertions.assertDoesNotThrow(
            ()->serviceToTest.cloneRepository("https://github.com/diging/scriptoocloud", "admin"));     
    }
        
    @Test
    public void test_cloneRepository_list() {
        Mockito.when(gitRepositoryJpa.findAll()).thenReturn((Iterable)new ArrayList<GitRepository>());
        Assertions.assertEquals(new ArrayList<GitRepository>(), serviceToTest.listRepositories());
    }   
        
    @Test
    public void test_cloneRepository_delete(){
        GitRepositoryImpl gitRepositoryImpl =  new GitRepositoryImpl();
        gitRepositoryImpl.setFolderName("test");
        Mockito.when(gitRepositoryJpa.findById((long) 0))
                .thenReturn(Optional.of(gitRepositoryImpl));
        Assertions.assertDoesNotThrow(()->serviceToTest.deleteRepository((long) 0));
    }   
}

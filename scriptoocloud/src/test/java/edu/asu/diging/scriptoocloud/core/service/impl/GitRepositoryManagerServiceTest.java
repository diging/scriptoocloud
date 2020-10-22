package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import edu.asu.diging.scriptoocloud.core.service.JgitService;

class GitRepositoryManagerServiceTest {

    @InjectMocks
    private GitRepositoryService serviceToTest;
    
    @Mock
    private JgitService jGitService;
    
    @Mock
    GitRepositoryRepository gitRepositoryJpa;
    
    private CloneForm cloneForm; 
    private SimpleUser user;
    private String mockPath = "C:/"; 
    
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);   
        cloneForm = new CloneForm();
        user = new SimpleUser();
        user.setUsername("testUser");
        cloneForm.setHost("github.com");
        cloneForm.setOwner("diging");
        cloneForm.setRepo("scriptoocloud");
        cloneForm.setUrl("https://github.com/diging/scriptoocloud");
        ReflectionTestUtils.setField(serviceToTest, "path", mockPath); 
    }
    
    @Test
    public void test_cloneRepository_clone() {
        Mockito.when(gitRepositoryJpa.findByUrl("")).thenReturn(new GitRepositoryImpl());
        Assertions.assertDoesNotThrow(()->serviceToTest.cloneRepository(cloneForm, user));
    }   
    
}

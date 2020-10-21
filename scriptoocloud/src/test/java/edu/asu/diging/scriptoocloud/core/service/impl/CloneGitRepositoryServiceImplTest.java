package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

class CloneGitRepositoryServiceImplTest {

    @InjectMocks
    private CloneGitRepositoryServiceImpl serviceToTest;
    
    private CloneForm cloneForm; 
    private SimpleUser user;
    private String mockPath = "C:/"; 
    private File file;
    
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
        file = new File(mockPath + user.getUsername() + cloneForm.getOwner() + cloneForm.getRepo());
    }

    @Test
    public void test_cloneRepo_duplicateClone() {
        Assertions.assertDoesNotThrow(()->serviceToTest.cloneRepo(cloneForm, user));
        Assertions.assertDoesNotThrow(()->serviceToTest.cloneRepo(cloneForm, user));
    }

}

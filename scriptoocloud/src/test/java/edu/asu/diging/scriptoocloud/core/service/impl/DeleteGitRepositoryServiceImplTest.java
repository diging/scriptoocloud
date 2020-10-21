package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.service.DeleteGitRepositoryService;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

class DeleteGitRepositoryServiceImplTest {

    @TempDir
    Path folder;

    @InjectMocks
    DeleteGitRepositoryServiceImpl serviceToTest;
    
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
        
    }

    @Test
    public void test_deleteDirectoryContents_deleteFolderAndContents() throws IOException{
        Files.write(folder.resolve("myfile"), "abc".getBytes());
        file = folder.resolve("myfile").toFile();
        Assertions.assertEquals(true, file.exists());
        serviceToTest.deleteDirectoryContents(file);
        Assertions.assertEquals(false, file.exists());
    }   
  
}

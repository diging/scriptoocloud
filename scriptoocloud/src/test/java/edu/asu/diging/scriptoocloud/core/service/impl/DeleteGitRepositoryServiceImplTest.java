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

class DeleteGitRepositoryServiceImplTest {

    @TempDir
    private Path folder;

    @InjectMocks
    private DeleteGitRepositoryServiceImpl serviceToTest;
    
    private File file;
    
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);   
        
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

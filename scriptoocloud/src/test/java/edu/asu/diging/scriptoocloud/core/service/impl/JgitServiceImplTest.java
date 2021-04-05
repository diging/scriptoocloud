package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.MalformedURLException;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;

/*
 * 
 * 
 * @author Jason Ormsby
*/

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AnnotationConfigContextLoader.class})
@TestPropertySource("classpath:config.properties")
class JgitServiceImplTest {

    @Value("${testPath}")
    private String path;

    @Mock
    private FileSystemService deleteFilesService;

    @InjectMocks
    private JgitServiceImpl serviceToTest;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);  
    }
    
    @Test
    public void test_clone_success() {

        String folderName = path + "clone_success";
        File file = new File(folderName);
        
        Assertions.assertDoesNotThrow(()->serviceToTest.clone(folderName, "https://github.com/diging/scriptoocloud"));
      
        Assertions.assertTrue(file.exists());

        recursiveDelete(file);
    }
    
    @Test
    public void test_clone_noSuchRepository() throws FileSystemStorageException {
        String folderName = path + "no_such_repository";
        File file = new File(folderName);

        Assertions.assertThrows(InvalidGitUrlException.class,
            ()->serviceToTest.clone(folderName, "github.com/test"));

        Mockito.verify(deleteFilesService).deleteDirectoryOrFile(Mockito.any());
        Assertions.assertTrue(file.exists());  
        recursiveDelete(file); 
        Assertions.assertFalse(file.exists());                 
    }   
    
    @Test
    public void test_clone_badUrl() throws FileSystemStorageException {
        String folderName = path + "bad_url";
        File file = new File(folderName);

        Assertions.assertThrows(InvalidGitUrlException.class,
            ()->serviceToTest.clone(folderName, "githubcom/test"));

        Mockito.verify(deleteFilesService).deleteDirectoryOrFile(Mockito.any());
        Assertions.assertTrue(file.exists());  
        recursiveDelete(file);
        Assertions.assertFalse(file.exists());      
    } 
    
    public void recursiveDelete(File file) {
        if(file.isDirectory()){
            File directorycontents[] = file.listFiles();
            for(File fileItem : directorycontents){
                recursiveDelete(fileItem);     
            }
            file.delete();
        }
        else{
            file.delete();
        }
    }

}
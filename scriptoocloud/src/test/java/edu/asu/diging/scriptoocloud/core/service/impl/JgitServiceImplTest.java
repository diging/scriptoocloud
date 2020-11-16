package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Properties;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.scriptoocloud.config.MvcConfig;
import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.service.DeleteFilesService;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AnnotationConfigContextLoader.class})
@TestPropertySource("classpath:config.properties")
class JgitServiceImplTest {
    
    @Value("${testPath}")
    private String path;
    
    @Mock
    private DeleteFilesService deleteFilesService;

    @InjectMocks
    private JgitServiceImpl serviceToTest;

    @Test
    public void test_clone_success() throws IOException{
        String folderName = path + "clone_success";
        File file = new File(folderName);
        
        Assertions.assertDoesNotThrow(()->serviceToTest.clone(folderName, "https://github.com/diging/scriptoocloud"));
      
        Assertions.assertTrue(file.exists());
        
        DeleteFilesServiceImpl deleteFilesService = new DeleteFilesServiceImpl();
        deleteFilesService.deleteDirectoryContents(file);
    }
    
    @Test
    public void test_clone_noSuchRepository(){
        String folderName = path + "no_such_repository";
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
        String folderName = path + "bad_url";
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
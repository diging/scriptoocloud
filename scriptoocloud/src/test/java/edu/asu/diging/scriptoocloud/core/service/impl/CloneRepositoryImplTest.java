package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;

import org.aspectj.apache.bcel.Repository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.eclipse.jgit.api.CloneCommand;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;

import edu.asu.diging.scriptoocloud.core.data.ClonedRepository;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.service.CloneRepository;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import edu.asu.diging.scriptoocloud.core.model.impl.RepositoryImpl;
import edu.asu.diging.scriptoocloud.core.data.ClonedRepository;
public class CloneRepositoryImplTest {


    @Mock
    private CloneCommand git;
    
    @Mock
    private org.eclipse.jgit.lib.Repository repository;
    
    @Mock
    ClonedRepository clonedRepository;
    
    @Mock
    private SecurityContext context;
    
    @Mock
    private CloneRepository cloneRepository;

    @InjectMocks
    private CloneRepositoryImpl serviceToTest;
    
    private CloneForm cloneForm; 
    private SimpleUser user;
    private Authentication authentication; 
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        cloneForm = new CloneForm();
        user = new SimpleUser();
        user.setUsername("testUser");
        cloneForm.setHost("github.com");
        cloneForm.setOwner("diging");
        cloneForm.setRepo("scriptoocloud");
        cloneForm.setUrl("https://github.com/diging/scriptoocloud");
        authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    @Test
    public void test_cloneRepo_cloneRepo(){
        Mockito.when( context.getAuthentication()).thenReturn(authentication);
        String result = serviceToTest.cloneRepo(cloneForm); 
        Assertions.assertEquals("redirect:/clone?success", result);
    }
    
    @Test
    public void test_deleteDirectoryContents_deleteRepo(){
        File parentDirectory = new File("C:/" + "testUser" + "_" + "diging" + "_" + "scriptoocloud");
        serviceToTest.deleteDirectoryContents(parentDirectory);
        parentDirectory = null;
        File file = new File("C:/testUser_diging_scriptoocloud");
        Assertions.assertEquals(false, file.exists());
    }   
  
    @Test
    public void test_cloneRepo_invalidRemoteRepository(){
        cloneForm.setUrl("https://github.com/@/scriptoocloud");
        Assertions.assertEquals("redirect:/clone?badurl", serviceToTest.cloneRepo(cloneForm));
    }   
  
    

}

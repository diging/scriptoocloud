package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;

/*
 * 
 * 
 * @author Jason Ormsby
*/

class UrlFormatterUtilityImplTest {

    @InjectMocks
    private UrlFormatterUtilityImpl serviceToTest;
    
    @Mock
    GitRepositoryRepository gitRepositoryJpa;
    
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);  
    } 
    
    @Test
    public void test_urlToFolderName_goodUrlWww() throws MalformedURLException {
        Assertions.assertEquals("www_website_com_path_path", serviceToTest.urlToFolderName("https://www.website.com/path/path"));
    }   
    
    @Test
    public void test_urlToFolderName_goodUrlNoWww() throws MalformedURLException {
        Assertions.assertEquals("website_com_path_path", serviceToTest.urlToFolderName("https://website.com/path/path"));
    }   
    
    @Test
    public void test_urlToFolderName_noProtocol() {
        Assertions.assertThrows(MalformedURLException.class, ()->serviceToTest.urlToFolderName("www.website.com/path/path") );
    }   
    
    @Test
    public void test_urlToFolderName_badUrl() {
        Assertions.assertThrows(MalformedURLException.class, ()->serviceToTest.urlToFolderName("website/path/path") );
    }   

}

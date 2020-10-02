package edu.asu.diging.scriptoocloud.core.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.scriptoocloud.core.service.ExampleServiceA;

public class ExampleServiceBImplTest {

    @Mock
    private ExampleServiceA serviceA;
    
    @InjectMocks
    private ExampleServiceBImpl serviceToTest;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_randomMultiply_positiveBase() {
        Mockito.when(serviceA.generateRandom()).thenReturn(5);
        int result = serviceToTest.randomMultiply(6);
        Assertions.assertEquals(30, result);
    }
    
    @Test
    public void test_randomMultiply_zeroBase() {
        Mockito.when(serviceA.generateRandom()).thenReturn(5);
        int result = serviceToTest.randomMultiply(0);
        Assertions.assertEquals(0, result);
    }
    
    @Test
    public void test_randomMultiply_negativeBase() {
        Mockito.when(serviceA.generateRandom()).thenReturn(5);
        int result = serviceToTest.randomMultiply(-1);
        Assertions.assertEquals(-5, result);
    }
    
    @Test
    public void test_randomMultiply_oneBase() {
        Mockito.when(serviceA.generateRandom()).thenReturn(5);
        int result = serviceToTest.randomMultiply(1);
        Assertions.assertEquals(5, result);
    }
}

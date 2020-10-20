package edu.asu.diging.scriptoocloud.core.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.scriptoocloud.core.service.ExampleServiceA;

public class ExampleServiceBImplTest {

    @Mock
    private ExampleServiceA serviceA;

    @InjectMocks
    private ExampleServiceBImpl serviceToTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_randomMultiply_positiveBase() {
        try (MockedStatic<ExampleServiceAImpl> mockedService = Mockito.mockStatic(ExampleServiceAImpl.class)) {
            mockedService.when(ExampleServiceAImpl::staticGenerate).thenReturn(5);
            int result = serviceToTest.randomMultiply(6);
            Assertions.assertEquals(30, result);
        }
    }

    @Test
    public void test_randomMultiply_zeroBase() {
        try (MockedStatic<ExampleServiceAImpl> mockedService = Mockito.mockStatic(ExampleServiceAImpl.class)) {
            mockedService.when(ExampleServiceAImpl::staticGenerate).thenReturn(5);
            int result = serviceToTest.randomMultiply(0);
            Assertions.assertEquals(0, result);
        }
    }

    @Test
    public void test_randomMultiply_negativeBase() {
        try (MockedStatic<ExampleServiceAImpl> mockedService = Mockito.mockStatic(ExampleServiceAImpl.class)) {
            mockedService.when(ExampleServiceAImpl::staticGenerate).thenReturn(5);
            int result = serviceToTest.randomMultiply(-1);
            Assertions.assertEquals(-5, result);
        }
    }

    @Test
    public void test_randomMultiply_oneBase() {
        try (MockedStatic<ExampleServiceAImpl> mockedService = Mockito.mockStatic(ExampleServiceAImpl.class)) {
            mockedService.when(ExampleServiceAImpl::staticGenerate).thenReturn(5);
            int result = serviceToTest.randomMultiply(1);
            Assertions.assertEquals(5, result);
        }
    }
}

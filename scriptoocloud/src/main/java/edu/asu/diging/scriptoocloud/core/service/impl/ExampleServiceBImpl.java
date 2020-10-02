package edu.asu.diging.scriptoocloud.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.ExampleServiceA;
import edu.asu.diging.scriptoocloud.core.service.ExampleServiceB;

@Service
public class ExampleServiceBImpl implements ExampleServiceB {

    @Autowired
    private ExampleServiceA serviceA;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.scriptoocloud.core.service.impl.ExampleServiceB#randomMultiply(int)
     */
    @Override
    public int randomMultiply(int base) {
        int factor = serviceA.generateRandom();
        return factor*base;
    }
}

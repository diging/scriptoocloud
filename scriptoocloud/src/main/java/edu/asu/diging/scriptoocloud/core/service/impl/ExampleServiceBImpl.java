package edu.asu.diging.scriptoocloud.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.ExampleServiceA;
import edu.asu.diging.scriptoocloud.core.service.ExampleServiceB;

@Service
public class ExampleServiceBImpl implements ExampleServiceB {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExampleServiceA serviceA;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.scriptoocloud.core.service.impl.ExampleServiceB#randomMultiply(int)
     */
    @Override
    public int randomMultiply(int base) {
        int factor = ExampleServiceAImpl.staticGenerate();
        logger.info(String.format("Multiplying with base %d and %d.", base, factor));
        return factor*base;
    }
}

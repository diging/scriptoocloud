package edu.asu.diging.scriptoocloud.core.service.impl;

import java.util.Random;

import org.springframework.stereotype.Service;

import edu.asu.diging.scriptoocloud.core.service.ExampleServiceA;

@Service
public class ExampleServiceAImpl implements ExampleServiceA {

    /* (non-Javadoc)
     * @see edu.asu.diging.scriptoocloud.core.service.impl.ExampleServiceA#generateRandom()
     */
    @Override
    public int generateRandom() {
        return new Random().nextInt();
    }
}

package edu.asu.diging.scriptoocloud.config;
import org.springframework.context.annotation.Configuration;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;
import edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration;

@Configuration
public class SimpleUsersConfig implements SimpleUsersConfiguration {

    @Override
    public void configure(SimpleUsers simpleUsers) {
     simpleUsers.resetRequestSentEndpoint("/reset/sent");
    }
    
}

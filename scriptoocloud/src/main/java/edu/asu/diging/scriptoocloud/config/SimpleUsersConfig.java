package edu.asu.diging.scriptoocloud.config;
import org.springframework.context.annotation.Configuration;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;
import edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration;

@Configuration
public class SimpleUsersConfig implements SimpleUsersConfiguration {

    @Override
    public void configure(SimpleUsers simpleUsers) {
    /*
         simpleUsers.emailUsername("");
         simpleUsers.emailPassword("");
         simpleUsers.emailServerHost("");
         simpleUsers.emailServerPort("");
  		 simpleUsers.emailFrom("");
  		 simpleUsers.instanceUrl(""); 
         simpleUsers.appName("");
     */
     
     simpleUsers.resetRequestSentEndpoint("/reset/sent");
    
    }
    
    
}
package edu.asu.diging.scriptoocloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;
import edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration;

@Configuration
@PropertySource("classpath:config.properties")
public class EmailConfig implements SimpleUsersConfiguration{

    @Autowired
    private Environment env;
    
    @Override
    public void configure(SimpleUsers simpleUsers) {
      simpleUsers.emailUsername(env.getRequiredProperty("email.user"));
      simpleUsers.emailPassword(env.getRequiredProperty("email.password"));
      simpleUsers.emailServerHost(env.getRequiredProperty("email.ServerHost"));
      simpleUsers.emailServerPort(env.getRequiredProperty("email.ServerPort"));
      simpleUsers.emailFrom(env.getRequiredProperty("email.emailFrom"));
      simpleUsers.instanceUrl(env.getRequiredProperty("email.instanceUrl")); 
      simpleUsers.emailStartTlsEnable(Boolean.parseBoolean(env.getRequiredProperty("email.tls")));
      simpleUsers.appName(env.getRequiredProperty("email.appName"));
    }

}
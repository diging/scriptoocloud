package edu.asu.diging.scriptoocloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;
import edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration;

@Configuration
@PropertySource("classpath:config.properties")
public class SimpleUsersConfig implements SimpleUsersConfiguration{

    @Autowired
    private Environment env;
    
    @Override
    public void configure(SimpleUsers simpleUsers) {
      simpleUsers.emailUsername(env.getRequiredProperty("email.user"))
      .emailPassword(env.getRequiredProperty("email.password"))
      .emailServerHost(env.getRequiredProperty("email.ServerHost"))
      .emailServerPort(env.getRequiredProperty("email.ServerPort"))
      .emailFrom(env.getRequiredProperty("email.emailFrom"))
      .instanceUrl(env.getRequiredProperty("email.instanceUrl"))
      .emailStartTlsEnable(Boolean.parseBoolean(env.getRequiredProperty("email.tls")))
      .appName(env.getRequiredProperty("email.appName"))
      .resetRequestSentEndpoint("/reset/sent");
    }

}
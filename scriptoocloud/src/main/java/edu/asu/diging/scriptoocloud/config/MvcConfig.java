package edu.asu.diging.scriptoocloud.config;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import edu.asu.diging.scriptoocloud.web.PasswordResetSentController;

@Configuration
@ComponentScan({"edu.asu.diging.scriptoocloud.web", "edu.asu.diging.simpleusers.web"})
public class MvcConfig {



    
}
package edu.asu.diging.scriptoocloud.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"edu.asu.diging.simpleusers.web","edu.asu.diging.scriptoocloud.core"})
public class MvcConfig {

}
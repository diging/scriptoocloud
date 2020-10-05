package edu.asu.diging.scriptoocloud.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import edu.asu.diging.scriptoocloud.core.service.impl.CloneRepositoryImpl;

@Configuration
@ComponentScan({"edu.asu.diging.simpleusers.web","edu.asu.diging.scriptoocloud.core"})
public class MvcConfig {


}
package edu.asu.diging.scriptoocloud.web.validation;

import java.net.URL;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryUrlConstraintValidator implements ConstraintValidator<RepositoryUrlConstraint, String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void initialize(RepositoryUrlConstraint constraintAnnotation) {}

    public boolean isValid(String urlField, ConstraintValidatorContext context) {
        try {
            URL url = new URL(urlField);
        } catch(Exception e){
            logger.warn("Malformed URL provided",e);
            return false;    
        }
        return true;
    }
}

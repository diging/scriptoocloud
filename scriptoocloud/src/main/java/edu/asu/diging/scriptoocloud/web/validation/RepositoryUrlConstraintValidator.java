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
        boolean valid = true;
        URL url;
        try{
         url = new URL(urlField);
        }
        catch(final Exception ignore){
            logger.error("Malformed URL provided");
            valid = false;    
        }
        return valid;
    }
}

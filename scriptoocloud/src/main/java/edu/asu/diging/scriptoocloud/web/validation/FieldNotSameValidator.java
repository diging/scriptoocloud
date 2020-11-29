package edu.asu.diging.scriptoocloud.web.validation;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;


public class FieldNotSameValidator implements ConstraintValidator<FieldNotSame, Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String oldName;
    private String newName;
    private String message;

    @Override
    public void initialize(final FieldNotSame constraintAnnotation) {
        oldName = constraintAnnotation.first();
        newName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            final Object firstObj = BeanUtils.getProperty(value, oldName);
            final Object secondObj = BeanUtils.getProperty(value, newName);

            valid = firstObj != null && secondObj != null && !firstObj.equals(secondObj);
        } catch (IllegalAccessException e) {
            logger.error("An IllegalAccessException Occurred in Validating Dataset edit. Ignoring.", e);
        } catch (InvocationTargetException e){
            logger.error("An InvocationTargetException Occurred in Validating Dataset edit. Ignoring.", e);
        } catch (NoSuchMethodException e){
            logger.error("An NoSuchMethodException Occurred in Validating Dataset edit. Ignoring.", e);
        }
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(oldName)
                    .addConstraintViolation().disableDefaultConstraintViolation();
        }
        return valid;
    }
}

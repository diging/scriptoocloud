package edu.asu.diging.scriptoocloud.web.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = RepositoryUrlConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryUrlConstraint {
    String message() default "Malformed URL Provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

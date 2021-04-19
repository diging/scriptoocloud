package edu.asu.diging.scriptoocloud.web.validation;


import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;

@Component
public class ProjectValidator implements Validator {

   @Override
   public boolean supports(Class<?> myClass) {
      return ProjectImpl.class.equals(myClass);
   }

   @Override
   public void validate(Object obj, Errors err) {

      ValidationUtils.rejectIfEmpty(err, "name", "project.name.empty");
      ValidationUtils.rejectIfEmpty(err, "description", "project.description.empty");

      /*
      ProjectImpl project = (ProjectImpl) obj;

      
      Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);
      if (!(pattern.matcher(user.getEmail()).matches())) {
         err.rejectValue("email", "user.email.invalid");
      }*/

   }

}

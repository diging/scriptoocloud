package edu.asu.diging.scriptoocloud.web.password;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.simpleusers.web.forms.UserForm;
 
@Controller
public class PasswordChangeController{

 	@RequestMapping(value = "/password/change", method=RequestMethod.GET)
    public String get(Model model) {
		model.addAttribute("user", new UserForm());
		return "/password/change";	
    }   

}
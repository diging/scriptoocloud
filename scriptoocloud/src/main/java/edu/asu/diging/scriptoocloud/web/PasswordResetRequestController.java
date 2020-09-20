package edu.asu.diging.scriptoocloud.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.web.RequestPasswordResetController;
import edu.asu.diging.simpleusers.web.forms.UserForm;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class PasswordResetRequestController {

    @RequestMapping(value = "/reset", method=RequestMethod.GET)
    public String get(Model model) {
		model.addAttribute("user", new UserForm());
		return "/reset/request";	
    } 
    
}
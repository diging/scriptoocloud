package edu.asu.diging.scriptoocloud.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.web.ResetPasswordInitiatedController;
import edu.asu.diging.simpleusers.web.forms.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
 
@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String home(Model model) {
    	return "home";
    	
    }
    
}
package edu.asu.diging.scriptoocloud.web.password;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class PasswordResetSentController{
	
    @RequestMapping(value = "/reset/sent", method=RequestMethod.GET)
    public String reset(Model model) {
        return "/reset/sent";
    }
   
}
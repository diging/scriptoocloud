package edu.asu.diging.scriptoocloud.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@PropertySource("classpath:/error.properties")
public class AccessDeniedController {

    @Value("${403}")
    private String accessDeniedErrorMessage;

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String error(Model model) {
        model.addAttribute("accessDeniedErrorMessage", "Http Error Code: " +
                accessDeniedErrorMessage);
        return "/403";
    }
}

package edu.asu.diging.scriptoocloud.web;

import org.springframework.core.env.Environment;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
@PropertySource("classpath:/error.properties")
public class ErrorController {

    private final Environment environment;

    public ErrorController(Environment environment) {
        this.environment = environment;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model, HttpServletResponse response) {
        model.addAttribute("httpErrorMessage", "Http Error Code: " + environment.getProperty(
                String.valueOf(response.getStatus()), "Unknown"));
        return "/error";
    }
}

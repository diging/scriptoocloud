package edu.asu.diging.scriptoocloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorController {
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model, HttpServletResponse response) {
        int httpErrorCode = response.getStatus();
        String errorMsg;
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 403: {
                errorMsg = "Http Error Code: 403. Forbidden";
                break;
            }
            case 404: {
                errorMsg = "Http Error Code: 404. Page not found";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
            default: {
                errorMsg = "There was an Http error when accessing this resource";
            }
        }
        model.addAttribute("httpErrorMessage", errorMsg);
        return "/error";
    }
}

package edu.asu.diging.scriptoocloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AddProjectController {

    @RequestMapping("/auth/projects/add")
    public String get() {
        return "auth/projects/add";
    }
}

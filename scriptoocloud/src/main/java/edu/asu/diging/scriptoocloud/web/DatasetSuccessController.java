package edu.asu.diging.scriptoocloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DatasetSuccessController {


    @GetMapping("/datasets/success")
    public String get(Model model) {
        return "datasets/success";
    }
}

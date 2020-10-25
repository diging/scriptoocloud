package edu.asu.diging.scriptoocloud.web;

import javax.persistence.NonUniqueResultException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.config.SimpleUsersConfig;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;


@Controller
public class RemoveProjectController {
    
    @Autowired
    private ProjectManager projectManager;

    @RequestMapping(value = "/auth/projects/remove", method=RequestMethod.GET)
    public String get(Model model) {
    	model.addAttribute("project", new ProjectImpl());
        return "/auth/projects/remove";
    }
    
    @RequestMapping(value = "/auth/projects/remove", method=RequestMethod.POST)
    public String post(@Valid @ModelAttribute("project") ProjectImpl projectImpl, BindingResult result, Model model, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("project", projectImpl);
        }

        projectManager.deleteProject(projectImpl.getName()); 
      
        return "redirect:/";
    }
}

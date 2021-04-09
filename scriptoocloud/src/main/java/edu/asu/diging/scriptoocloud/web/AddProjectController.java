package edu.asu.diging.scriptoocloud.web;

import java.security.Principal;

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
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;

@Controller
public class AddProjectController {

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private IUserManager userManager;

    public AddProjectController() {
        
    }

    @RequestMapping(value = "/auth/projects/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("project", new ProjectImpl());
        return "/auth/projects/add";
    }

    @RequestMapping(value = "/auth/projects/add", method = RequestMethod.POST)
    public String post(@Valid @ModelAttribute("project") ProjectImpl projectImpl, BindingResult result, Model model,
            RedirectAttributes redirectAttrs, Principal principal) {

        String username = principal.getName();
        IUser user = userManager.findByUsername(username);
        
        if(projectImpl.getName().isEmpty() && projectImpl.getDescription().isEmpty()) {
            model.addAttribute("errorMessage", "Please enter a name and description.");
            return "/auth/projects/add";
        }
        if(projectImpl.getName().isEmpty()) {
            model.addAttribute("errorMessage", "Please enter a name.");
            return "/auth/projects/add";
        }
        if(projectImpl.getDescription().isEmpty()) {
            model.addAttribute("errorMessage", "Please enter a description.");
            return "/auth/projects/add";
        }
        
        Project project = projectManager.createProject(projectImpl.getName(), projectImpl.getDescription(), user);
        redirectAttrs.addAttribute("addSuccessMessage", "Project sucessfully added.");
        return "redirect:/auth/projects";
    }
}

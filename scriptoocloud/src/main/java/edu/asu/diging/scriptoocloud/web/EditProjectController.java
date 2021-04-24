package edu.asu.diging.scriptoocloud.web;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.core.data.ProjectRepository;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;
import edu.asu.diging.scriptoocloud.web.validation.ProjectValidator;

@Controller
public class EditProjectController {

    @Autowired
    private ProjectManager projectManager;
    
    @Autowired
    private ProjectValidator projectValidator;
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
       binder.addValidators(projectValidator);
    }
    
    @RequestMapping(value = "/auth/edit/{id}", method = RequestMethod.GET)
    public String get(Model model,@PathVariable("id") int id) {
        ProjectImpl project = (ProjectImpl) projectManager.getProject(id);
        model.addAttribute("project", project);
        return "/auth/edit";
    }

    @RequestMapping(value = "/auth/edit/{id}", method = RequestMethod.POST)
    public String post(@Valid @ModelAttribute("project") ProjectImpl projectImpl, BindingResult result,
            Model model, RedirectAttributes redirectAttrs, Principal principal,@PathVariable("id") int id) {
        if (result.hasErrors()) {
            model.addAttribute("projectImpl", new ProjectImpl());
            return "/auth/edit";
        }
        projectManager.updateProject(id, projectImpl.getName(), projectImpl.getDescription());
        redirectAttrs.addAttribute("successMessage", "Project sucessfully updated.");
        return "redirect:/auth/projects";
    }
}

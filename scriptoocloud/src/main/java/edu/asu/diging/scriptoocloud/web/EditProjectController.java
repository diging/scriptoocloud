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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.core.data.ProjectRepository;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;

@Controller
public class EditProjectController {

    @Autowired
    private ProjectManager projectManager;
    
    private int projectID;

    @RequestMapping(value = "/auth/edit/{id}", method = RequestMethod.POST)
    public String post(@Valid @ModelAttribute("project") Optional<ProjectImpl> projectImpl, BindingResult result,
            Model model, RedirectAttributes redirectAttrs, Principal principal, @PathVariable("id") int id) {
        if (result.hasErrors()) {
            model.addAttribute("project", new ProjectImpl());
            return "/auth/edit/{id}";
        }
        projectImpl = projectManager.getProject(id);
        projectID = id;
        model.addAttribute("project", projectImpl);
        return "/auth/edit";
    }

    @RequestMapping(value = "/auth/edit", method = RequestMethod.POST)
    public String post2(@Valid @ModelAttribute("project") ProjectImpl projectImpl, BindingResult result,
            Model model, RedirectAttributes redirectAttrs, Principal principal) {
        ProjectImpl project = projectImpl;
        projectManager.updateProject((int) projectID, project.getName(), project.getDescription());
        return "redirect:/auth/projects";
    }
}

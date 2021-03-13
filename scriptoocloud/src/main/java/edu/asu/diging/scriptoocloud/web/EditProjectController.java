package edu.asu.diging.scriptoocloud.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;

@Controller
public class EditProjectController {
    
    @Autowired
    private ProjectManager projectManager;

    @RequestMapping(value = "/auth/edit/", method=RequestMethod.GET)
    public String get(Model model) {
    	model.addAttribute("project", new ProjectImpl());
        return "/auth/edit";
    }
    
    @RequestMapping(value = "/auth/edit/{id}", method=RequestMethod.POST)
    public String post(@PathVariable("id") int id,Model model) {
        
    	Project project = projectManager.getProject(id);
    	
    	
        return "/auth/edit";
    }
}


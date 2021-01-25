package edu.asu.diging.scriptoocloud.web;

import javax.persistence.NonUniqueResultException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.config.SimpleUsersConfig;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;
import edu.asu.diging.scriptoocloud.core.service.impl.nullIDException;


@Controller
public class RemoveProjectController {
    
    @Autowired
    private ProjectManager projectManager;
    
    @RequestMapping(value = "/auth/projects/remove", method=RequestMethod.GET)
    public String get(Model model) {
        return "/auth/projects/remove";
    }              
    
    @RequestMapping(value = "/auth/projects/remove/{id}", method=RequestMethod.POST)
    public String post(@PathVariable("id") int id,Model model) {
        
    	try {
        	projectManager.deleteProject(id); 
        	return "redirect:/";
        }catch (nullIDException err) {
        	model.addAttribute("deletionFailure","ID is NULL");
        	return "redirect:/";
        }
      
    }
}

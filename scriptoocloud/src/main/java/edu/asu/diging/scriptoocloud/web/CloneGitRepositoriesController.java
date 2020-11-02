package edu.asu.diging.scriptoocloud.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;
import edu.asu.diging.simpleusers.core.model.IUser;


@Controller
public class CloneGitRepositoriesController {
    
    @Autowired
    GitRepositoryManager gitRepositoryManager;
    
    @Value("${git.repositories.path}")
    private String path;
  
    private static Logger logger = LoggerFactory.getLogger(CloneGitRepositoriesController.class);    
    
    @RequestMapping(value = "/repositories/clone", method = RequestMethod.GET)
    public String clone(Model model) {
        model.addAttribute("clone", new CloneForm());
        return "repositories/clone";
    }
  
    @RequestMapping(value = "/repositories/clone", method = RequestMethod.POST)
    public String clone(@Valid @ModelAttribute("clone") CloneForm cloneForm, BindingResult result, 
                            Model model){                                          
        
        if(result.hasErrors()){
            model.addAttribute("clone",cloneForm);
            return "redirect:/repositories/clone" + "?BadForm";
        }                 
        
        IUser user = (IUser)SecurityContextHolder.getContext()
                            .getAuthentication().getPrincipal();       
                            
        try{                     
            gitRepositoryManager.cloneRepository(cloneForm, user);       
        }
        catch(IllegalArgumentException e){
            logger.error("No git repository found at provided URL" 
                            + path + user + cloneForm.getOwner() + cloneForm.getRepo());
            return "redirect:/repositories/clone" + "?badurl";
       }
       
       return "redirect:/repositories/clone" + "?success";
    }
    
    
}
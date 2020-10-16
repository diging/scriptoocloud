package edu.asu.diging.scriptoocloud.web;

import javax.validation.Valid;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.service.CloneRepository;

@Controller
public class CloneRepositoryController {

    @Autowired
    CloneRepository cloneService;
 
    @RequestMapping(value = "/listrepos", method = RequestMethod.GET)
    public String listRepos(Model model) {
        model.addAttribute("repo",cloneService.getRepos());
        return  "github/ListRepos";
    }

    @RequestMapping(value = "/listrepos/{host}/{owner}/{repo}/{requester}", method = RequestMethod.POST)
    public String deleteRepo(@PathVariable("host") String host,
                        @PathVariable("owner") String owner,
                        @PathVariable("repo") String repo,
                        @PathVariable("requester") String requester,
                        @ModelAttribute("clone") CloneForm cloneForm, BindingResult result) 
                             throws GitAPIException {        
       return cloneService.deleteRepo(host + "/" + owner + "/" + repo, requester, owner, repo);        
    }
  
    @RequestMapping(value = "/clone", method = RequestMethod.GET)
    public String clone(Model model) {
        model.addAttribute("clone", new CloneForm());
        return "github/clone";
    }
  
    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String clone(@Valid @ModelAttribute("clone") CloneForm cloneForm, BindingResult result, Model model) 
                             throws GitAPIException {                                          
        if(result.hasErrors()){
            model.addAttribute("clone", new CloneForm());
              return "redirect:/clone" + "?BadForm";
        }                 
        return cloneService.cloneRepo(cloneForm);       
    }
    
    
}
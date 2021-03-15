package edu.asu.diging.scriptoocloud.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;

@Controller
public class RunRepositoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());   

    @Autowired
    private GitRepositoryManager gitRepositoryManager;

    @RequestMapping(value = "/auth/list/{projectId}", method = RequestMethod.GET)
    public String listRepos(@PathVariable("projectId") int projectId, RedirectAttributes redirectAttribute, Model model) {
        model.addAttribute("repos", gitRepositoryManager.listRepositories());
        redirectAttribute.addAttribute("projectId",projectId);
        return "/auth/list";
    }  

    @RequestMapping(value = "/user/repositories/run/{repoId}", method = RequestMethod.GET)
    public String runRepo(@ModelAttribute("projectId") int projectId, @PathVariable("repoId") int repoId,  Model model) {
    
        System.out.println(projectId);
        System.out.println(repoId);
        
        model.addAttribute("repos", gitRepositoryManager.listRepositories());

        return "/user/list";
    }  

    
}
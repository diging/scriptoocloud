package edu.asu.diging.scriptoocloud.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;

@Controller
public class DeleteGitRepositoryController {

    @Autowired
    private GitRepositoryManager gitRepositoryManager;

    @RequestMapping(value = "/repositories/delete/{id}", method = RequestMethod.POST)
    public String deleteRepo(@PathVariable("id") Long id) {   
        gitRepositoryManager.deleteRepository(id);
        return "redirect:/repositories/list" + "?success";            
    }
  
}
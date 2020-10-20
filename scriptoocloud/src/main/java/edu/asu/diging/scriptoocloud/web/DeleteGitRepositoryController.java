package edu.asu.diging.scriptoocloud.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.service.DeleteGitRepositoryService;

@Controller
public class DeleteGitRepositoryController {

    @Autowired 
    private GitRepositoryRepository gitRepositoryJpa;
    
    @Autowired
    private DeleteGitRepositoryService deleteGitService;

    @RequestMapping(value = "/repositories/delete", method = RequestMethod.POST)
    public String deleteRepo(@ModelAttribute("repository") GitRepository gitRepository, BindingResult result) {    
        deleteGitService.deleteRepository(gitRepository);
        gitRepositoryJpa.deleteById(gitRepository.getId());
        return "redirect:repositories/list" + "?success";            
    }
  
}
package edu.asu.diging.scriptoocloud.web;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.service.ListGitRepositoriesService;

@Controller
public class ListGitRepositoriesController {

    @Autowired 
    private ListGitRepositoriesService listRepositoriesService;
 
    @RequestMapping(value = "/repositories/list", method = RequestMethod.GET)
    public String listRepos(Model model) {
        ArrayList<GitRepository> repositories = listRepositoriesService.getRepos();
        model.addAttribute("repos", repositories);
        return "repositories/list";
    }  

}
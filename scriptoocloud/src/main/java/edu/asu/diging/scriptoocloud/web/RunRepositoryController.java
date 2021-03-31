package edu.asu.diging.scriptoocloud.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.core.model.impl.YamlModel;
import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;
import edu.asu.diging.scriptoocloud.core.service.IYamlParserService;
import edu.asu.diging.scriptoocloud.core.service.impl.DockerService;
@Controller
public class RunRepositoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());   

    @Autowired
    private GitRepositoryManager gitRepositoryManager;
    
    @Autowired
    private IYamlParserService yamlParserService;

    @Autowired
    private DockerService dockerService;


  @RequestMapping(value = "/auth/run/{repoId}/{projectID}", method = RequestMethod.GET)
    public String runRepo(Model model, @PathVariable("repoId") Long repoId, @PathVariable("projectID") Long projectID ) throws FileNotFoundException { 
        
        String repoPath = gitRepositoryManager.getRepositoryPath(repoId);
     
        YamlModel yamlModel = yamlParserService.parseYamlInDirectory(repoPath);
      
        model.addAttribute("yamlModel", yamlModel);
        model.addAttribute("repoId", repoId);

        return "/auth/run";
    }  


    @RequestMapping(value = "/auth/run/{repoId}", method = RequestMethod.POST)
    public String runRepoPost(Model model, @ModelAttribute("yamlModel") YamlModel yamlModel, @PathVariable("repoId") Long repoId) throws FileNotFoundException {
        System.out.println(repoId);

        try {  

        String containerId = dockerService.buildContainer(gitRepositoryManager.getRepositoryImageId(repoId), yamlModel.getInputParams());

        dockerService.runContainer(containerId);

       } catch (Exception e) {e.printStackTrace();}


        return "redirect:/auth/projects";
    }  

    
}
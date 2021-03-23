package edu.asu.diging.scriptoocloud.web;

import java.io.File;
import java.io.FileNotFoundException;
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
@Controller
public class RunRepositoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());   

    @Autowired
    private GitRepositoryManager gitRepositoryManager;
    
    @Autowired
    private IYamlParserService yamlParserService;



  @RequestMapping(value = "/auth/run/{repoId}/{projectID}", method = RequestMethod.GET)
   // public String runRepo(@ModelAttribute("projectId") int projectId, @PathVariable("repoId") int repoId,  Model model) throws FileNotFoundException {
    public String runRepo(Model model,@PathVariable("repoId") Long repoId, @PathVariable("repoId") Long projectID ) throws FileNotFoundException {
    
   
      //System.out.println(attribute.getFlashAttributes().get("projectId"));
     

     
        //parse yaml and generate model

       
        String repoPath = gitRepositoryManager.getRepositoryPath(repoId);
        
        YamlModel yamlModel = yamlParserService.parseYamlInDirectory(repoPath);
        
        model.addAttribute("yamlModel", yamlModel);

        return "/auth/run";
    }  


    @RequestMapping(value = "/auth/run/{repoId}", method = RequestMethod.POST)
   // public String runRepo(@ModelAttribute("projectId") int projectId, @PathVariable("repoId") int repoId,  Model model) throws FileNotFoundException {
    public String runRepoPost(Model model) throws FileNotFoundException {
    
    /* get model dynamic
        System.out.println(projectId);
        System.out.println(repoId);
        yamlParserService.parseYaml(new File(gitRepositoryManager.getRepositoryPath((long)repoId)));
      */
      
       // Map<String, Object> stcYamlKeyPair = yamlParserService.parseYamlFile(new File("C:/github_com_jormsby2_CloneTest/test.yml"));

        
      

        return "/auth/run";
    }  

    
}
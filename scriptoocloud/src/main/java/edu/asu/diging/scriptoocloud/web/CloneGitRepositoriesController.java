package edu.asu.diging.scriptoocloud.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;

import javax.validation.Valid;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.forms.CloneForm;
import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;

@Controller
public class CloneGitRepositoriesController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GitRepositoryManager gitRepositoryManager;


    @RequestMapping(value = "/admin/repositories/clone", method = RequestMethod.GET)
    public String clone(Model model) {
        model.addAttribute("clone", new CloneForm());
        return "/admin/repositories/clone";
    }

    @RequestMapping(value = "/admin/repositories/clone", method = RequestMethod.POST)
    public String clone(@Valid @ModelAttribute("clone") CloneForm cloneForm, BindingResult result,
                        RedirectAttributes redirectAttributes, Model model, Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("clone", cloneForm);
            return "/admin/repositories/clone";
        }

        String user = principal.getName();

        try {
            gitRepositoryManager.cloneRepository(cloneForm.getUrl(), user);
        } catch (InvalidGitUrlException e) {
            logger.error("No git repository found at provided URL " + cloneForm.getUrl());
            model.addAttribute("formResponseFailure", "No such git repository found");
            model.addAttribute("clone", cloneForm);
            return "/admin/repositories/clone";
        } catch (MalformedURLException e) {
            logger.error("Malformed URL made it past validator " + cloneForm.getUrl());
            model.addAttribute("formResponseFailure", "Provided URL is Malformed");
            model.addAttribute("clone", cloneForm);
            return "/admin/repositories/clone";
        } catch (JGitInternalException e) {
            logger.error("JGit command execution failure, check system configurations ", e);
            model.addAttribute("formResponseFailure","JGit internal failure");
            model.addAttribute("clone",cloneForm);
            return "/admin/repositories/clone";        
        } catch (IOException e) {
            logger.error("Failed to archive directory", e);
            model.addAttribute("formResponseFailure","Failed to archive directory");
            model.addAttribute("clone",cloneForm);
            return "/admin/repositories/clone";   
        } catch (InterruptedException e) {
            logger.error("Failed to create docker image ", e);
            model.addAttribute("formResponseFailure","Failed to create docker image");
            model.addAttribute("clone",cloneForm);
            return "/admin/repositories/clone";   
        }
        catch (FileSystemStorageException e) {
            logger.error("Error deleting existing repository on the File System", e);
            model.addAttribute("formResponseFailure", "Error deleting existing repository");
            model.addAttribute("clone", cloneForm);
            return "/admin/repositories/clone";
        }
       redirectAttributes.addAttribute("formResponse","Successfully cloned");
       return "redirect:/admin/repositories/clone";

    }

}
package edu.asu.diging.scriptoocloud.web;

import javax.persistence.NonUniqueResultException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
public class RemoveProjectController {

    @Autowired
    private ProjectManager projectManager;

    @RequestMapping(value = "/auth/projects/{id}/remove", method = RequestMethod.POST)
    public String post(@PathVariable("id") Long id, Model model) {

        projectManager.deleteProject(id);
        return "redirect:/auth/projects";
    }
}

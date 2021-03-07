package edu.asu.diging.scriptoocloud.web;

import java.util.Iterator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.scriptoocloud.core.data.ProjectRepository;
import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.scriptoocloud.core.model.impl.ProjectImpl;
import edu.asu.diging.scriptoocloud.core.service.ProjectManager;

@Controller
public class ProjectsController {

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private RemoveProjectController removeProjectController;

	@Autowired
	private AddProjectController addProjectController;

	@Autowired
	private ProjectManager projectManager;

	@RequestMapping("/auth/projects")
	public String listProjects(Model model) {
		model.addAttribute("projects", projectManager.findAll());
		return "/auth/projects";
	}
}
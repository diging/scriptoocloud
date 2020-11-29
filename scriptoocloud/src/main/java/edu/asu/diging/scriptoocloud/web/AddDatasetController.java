package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.scriptoocloud.web.forms.DatasetEditForm;
import edu.asu.diging.scriptoocloud.web.forms.DatasetForm;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.security.Principal;

@Controller
public class AddDatasetController {

    private final IDatasetService iDatasetService;
    private final IUserManager userManager;

    @Autowired
    public AddDatasetController(IDatasetService iDatasetService, IUserManager userManager) {
        this.iDatasetService = iDatasetService;
        this.userManager = userManager;
    }

    @RequestMapping(value = "datasets/list", method = RequestMethod.GET)
    public String get(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("dataset", new DatasetForm());
        model.addAttribute("datasetEditForm", new DatasetEditForm());
        IUser user = userManager.findByUsername(username);
        Page<Dataset> dbDatasets = iDatasetService.findAllByUser(user);
        model.addAttribute("dbDatasets", dbDatasets);
        return "datasets/list";
    }

    @RequestMapping(value = "datasets/add", method = RequestMethod.POST)
    public String post(@Valid @ModelAttribute("dataset") DatasetForm datasetForm,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model,
                       Principal principal) {
        String username = principal.getName();
        IUser user = userManager.findByUsername(username);
        datasetForm.setUsername(username);
        if (result.hasErrors()) {
            Page<Dataset> dbDatasets = iDatasetService.findAllByUser(user);
            model.addAttribute("dbDatasets", dbDatasets);
            model.addAttribute("dataset", datasetForm);
            model.addAttribute("datasetEditForm", new DatasetEditForm());
            return "datasets/list";
        }
        try {
            iDatasetService.createDataset(datasetForm.getName(), user);
        } catch (DatasetStorageException e) {
            Page<Dataset> dbDatasets = iDatasetService.findAllByUser(user);
            model.addAttribute("dbDatasets", dbDatasets);
            model.addAttribute("dataset", datasetForm);
            model.addAttribute("datasetEditForm", new DatasetEditForm());
            model.addAttribute("errorMessage", "There was an error creating the dataset");
            return "datasets/list";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Dataset successfully created");
        return "redirect:/datasets/list";
    }
}

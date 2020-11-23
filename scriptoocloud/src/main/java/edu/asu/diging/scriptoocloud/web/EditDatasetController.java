package edu.asu.diging.scriptoocloud.web;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class EditDatasetController {

    private final IDatasetService iDatasetService;
    private final IUserManager userManager;

    @Autowired
    public EditDatasetController(IDatasetService iDatasetService, IUserManager userManager) {
        this.iDatasetService = iDatasetService;
        this.userManager = userManager;
    }

    @RequestMapping(value = "datasets/edit", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute("datasetEditForm") DatasetEditForm datasetEditForm,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Principal principal,
                       Model model) {
        // Duplicate Dataset names are okay, but editing a Dataset to have the same name
        // that it already has (same id, same name) is an error.
        if (result.hasErrors()){
            String username = principal.getName();
            IUser user = userManager.findByUsername(username);
            Page<Dataset> dbDatasets = iDatasetService.findAllByUser(user);
            model.addAttribute("dbDatasets", dbDatasets);
            model.addAttribute("dataset", new DatasetForm());
            model.addAttribute("datasetEditForm", datasetEditForm);
            model.addAttribute("errorIndex", datasetEditForm.getIndex());
            return "datasets/list";
        } else {
            iDatasetService.editDataset(datasetEditForm.getId(), datasetEditForm.getNewName());
        }
        redirectAttributes.addFlashAttribute("successMessage", "Dataset successfully edited");
        return "redirect:/datasets/list";
    }
}

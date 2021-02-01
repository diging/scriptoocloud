package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class DeleteDatasetController {
    private final IDatasetService iDatasetService;

    @Autowired
    public DeleteDatasetController(IDatasetService iDatasetService) {
        this.iDatasetService = iDatasetService;
    }

    @RequestMapping(value = "datasets/{id}/delete", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#datasetId, 'Dataset', 'delete')")
    public String delete(@PathVariable("id") Long datasetId,
                         RedirectAttributes redirectAttributes,
                         Model model,
                         Principal principal) {
        String username = principal.getName();
        try {
            iDatasetService.deleteDataset(datasetId, username);
        } catch (DatasetStorageException e) {
            model.addAttribute("ErrorMessage", "Error: Could not delete Dataset");
            return "redirect:/datasets/list";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Dataset successfully deleted");
        return "redirect:/datasets/list";
    }
}

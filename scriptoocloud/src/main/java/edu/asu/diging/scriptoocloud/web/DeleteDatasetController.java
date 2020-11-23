package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class DeleteDatasetController {
    private final IDatasetService iDatasetService;

    @Autowired
    public DeleteDatasetController(IDatasetService iDatasetService) {
        this.iDatasetService = iDatasetService;
    }

    @RequestMapping(value = "datasets/delete", method = RequestMethod.POST)
    public String delete(RedirectAttributes redirectAttributes,
                         HttpServletRequest request,
                         Model model,
                         Principal principal) {
        String username = principal.getName();
        try {
            iDatasetService.deleteDataset(Long.parseLong(request.getParameter("delSetId")), username);
        } catch (DatasetStorageException e) {
            model.addAttribute("ErrorMessage", "Error: Could not delete Dataset");
            return "datasets/list";
        } catch (NumberFormatException e) {
            model.addAttribute("ErrorMessage", "Error: Dataset index not found");
            return "datasets/list";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Dataset successfully deleted");
        return "redirect:/datasets/list";
    }
}

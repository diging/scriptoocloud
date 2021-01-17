package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteDataFileController {
    private final IDatasetService iDatasetService;

    @Autowired
    public DeleteDataFileController(IDatasetService iDatasetService) {
        this.iDatasetService = iDatasetService;
    }

    @RequestMapping(value = "datasets/{id}/files/{fileId}/delete", method = RequestMethod.POST)
    public String deleteFile(@PathVariable("id") String datasetIdString,
                             @PathVariable("fileId") String fileId,
                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("id", datasetIdString);
        try {
            Long datasetId = Long.parseLong(datasetIdString);
            iDatasetService.deleteFileFromDataset(datasetId, Long.parseLong(fileId));
        } catch (DatasetStorageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: File could not be deleted");
            return "redirect:/datasets/{id}";
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: File could not be deleted. Id could not be parsed to a Long value");
            return "redirect:/datasets/{id}";
        }
        redirectAttributes.addFlashAttribute("successMessage", "File Successfully Deleted");
        return "redirect:/datasets/{id}";
    }
}

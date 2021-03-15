package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DataFileNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteDataFileController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IDatasetService iDatasetService;

    @Autowired
    public DeleteDataFileController(IDatasetService iDatasetService) {
        this.iDatasetService = iDatasetService;
    }

    @RequestMapping(value = "auth/datasets/{id}/files/{fileId}/delete", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#fileId, 'DataFile', 'delete')")
    public String deleteFile(@PathVariable("id") String datasetIdString,
                             @PathVariable("fileId") Long fileId,
                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("id", datasetIdString);
        try {
            Long datasetId = Long.parseLong(datasetIdString);
            iDatasetService.deleteFileFromDataset(datasetId, fileId);
        } catch (DatasetStorageException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: File could not be deleted");
            logger.error("ERROR: DataFile could not be deleted", e);
            return "redirect:/auth/datasets/{id}";
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: File could not be deleted. Id could not be parsed to a Long value");
            logger.error("ERROR: DataFile could not be deleted. Id could not be parsed to a Long", e);
            return "redirect:/auth/datasets/{id}";
        } catch (DatasetNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dataset not found");
            logger.error("ERROR: Dataset not found when attempting to delete DataFile", e);
        } catch (DataFileNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "DataFile not found");
            logger.error("ERROR: DataFile not found when attempting to delete DataFile", e);
        }
        redirectAttributes.addFlashAttribute("successMessage", "File Successfully Deleted");
        return "redirect:/auth/datasets/{id}";
    }
}

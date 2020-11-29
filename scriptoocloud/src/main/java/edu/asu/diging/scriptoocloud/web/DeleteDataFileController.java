package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DeleteDataFileController {
    private final IDatasetService iDatasetService;

    @Autowired
    public DeleteDataFileController(IDatasetService iDatasetService) {
        this.iDatasetService = iDatasetService;
    }

    @RequestMapping(value = "datasets/deleteFile", method = RequestMethod.POST)
    public String deleteFile(HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        String datasetIdString = request.getParameter("datasetId");
        try {
            Long fileId = Long.parseLong(request.getParameter("fileId"));
            Long datasetId = Long.parseLong(datasetIdString);
            iDatasetService.deleteFileFromDataset(datasetId, fileId);
        } catch (DatasetStorageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: File could not be deleted");
            return "redirect:/datasets/" + datasetIdString;
        } catch (NumberFormatException e){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: File could not be deleted. Id could not be parsed to a Long value");
            return "redirect:/datasets/" + datasetIdString;
        }
        redirectAttributes.addFlashAttribute("successMessage", "File Successfully Deleted");
        return "redirect:/datasets/" + datasetIdString;
    }
}

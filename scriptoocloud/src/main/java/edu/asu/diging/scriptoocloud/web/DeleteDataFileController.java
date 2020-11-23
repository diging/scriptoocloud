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

        Long fileId = Long.parseLong(request.getParameter("fileId"));
        Long datasetId = Long.parseLong(request.getParameter("datasetId"));
        try {
            iDatasetService.deleteFileFromDataset(datasetId, fileId);
        } catch (DatasetStorageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: File could not be deleted");
        }
        redirectAttributes.addFlashAttribute("successMessage", "File Successfully Deleted");
        return "redirect:/datasets/" + datasetId;
    }
}

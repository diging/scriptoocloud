package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.service.IDataFileService;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.scriptoocloud.core.service.impl.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@Controller
public class AddDataFileController {

    private final IDatasetService iDatasetService;
    private final IDataFileService iDataFileService;

    @Autowired
    public AddDataFileController(IDatasetService iDatasetService,
                                 IDataFileService iDataFileService) {
        this.iDatasetService = iDatasetService;
        this.iDataFileService = iDataFileService;
    }

    @RequestMapping(value = "datasets/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             Principal principal,
                             Model model) {
        String username = principal.getName();
        String datasetIdString = request.getParameter("fileUploadDatasetId");
        Long datasetId = Long.parseLong(datasetIdString);
        if (!multipartFile.isEmpty()) {
            model.addAttribute("file", multipartFile);
        } else {
            redirectAttributes.addFlashAttribute("noFileMessage", "Please Choose a File");
            return "redirect:/datasets/" + datasetId;
        }
        try {
            byte[] bytes = multipartFile.getBytes();
            iDataFileService.createFile(bytes, datasetIdString, username,
                    multipartFile.getOriginalFilename(), multipartFile.getContentType());
            redirectAttributes.addFlashAttribute("successMessage", "File Successfully Uploaded");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: File could not be Uploaded");
        }
        return "redirect:/datasets/" + datasetId;
    }
}

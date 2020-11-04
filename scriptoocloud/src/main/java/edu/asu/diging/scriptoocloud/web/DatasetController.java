package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.scriptoocloud.core.service.impl.DataFileService;
import edu.asu.diging.scriptoocloud.core.service.impl.DatasetService;
import edu.asu.diging.scriptoocloud.web.forms.DatasetForm;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Controller
public class DatasetController {

    private final DatasetService datasetService;
    private final DataFileService dataFileService;
    private final IDatasetService datasetManager;
    private final IUserManager userManager;

    @Autowired
    public DatasetController(DatasetService datasetService,
                             DataFileService dataFileService,
                             IDatasetService datasetManager,
                             IUserManager userManager) {
        this.datasetService = datasetService;
        this.dataFileService = dataFileService;
        this.datasetManager = datasetManager;
        this.userManager = userManager;
    }

    @RequestMapping(value = "datasets/{username}", method = RequestMethod.GET)
    public String get(Model model, @PathVariable String username) {
        model.addAttribute("dataset", new DatasetForm());
        List<IDataset> dbDatasets = datasetService.findAllByUsername(username);
        if (dbDatasets.size() > 0) {
            model.addAttribute("dbDatasets", dbDatasets);
        } else {
            model.addAttribute("noDatasets", "You Have No Datasets Yet");
        }
        return "datasets/index";
    }

    @RequestMapping(value = "datasets/add", method = RequestMethod.POST)
    public String post(@Valid @ModelAttribute("dataset") DatasetForm datasetForm,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        boolean success;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        IUser user = userManager.findByUsername(username);
        datasetForm.setUsername(username);

        if (result.hasErrors()) {
            // Handle form error - They don't show up automatically with redirects
            if (datasetForm.getName().isEmpty()) {
                redirectAttributes.addFlashAttribute("noDatasetName", "Dataset Name Cannot Be Empty");
            }
            redirectAttributes.addFlashAttribute("dataset", datasetForm);
            redirectAttributes.addAttribute("username", username);
            return "redirect:/datasets/{username}";
        }

        try {
            success = datasetService.createDataset(datasetForm.getName(), user);
        } catch (DatasetException e) {
            model.addAttribute("dataset", new DatasetForm());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("username", username);
            return "redirect:/datasets/{username}";
        }

        if (success){
            redirectAttributes.addFlashAttribute("message", "Dataset successfully created");
        }
        return "redirect:/datasets/success";
    }

    @RequestMapping(value = "datasets/delete", method = RequestMethod.POST)
    public String delete(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("delSetId"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean success;
        try {
            success = datasetService.deleteDataset(id);
        } catch (DatasetException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("username", username);
            return "redirect:/datasets/{username}";
        }
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Dataset successfully deleted");
        }
        return "redirect:/datasets/success";
    }

    @RequestMapping(value = "datasets/edit", method = RequestMethod.POST)
    public String edit(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        boolean success;
        Long id = Long.parseLong(request.getParameter("editSetId"));
        String newName = request.getParameter("editSetName");
        IDataset oldDataset = datasetManager.findById(id);
        String oldName = oldDataset.getName();
        String username = oldDataset.getUsername();
        String index = request.getParameter("editIndex");

        if (oldName.equals(newName)) {
            // Show error at correct index
            redirectAttributes.addFlashAttribute("errorIndex", index);
            redirectAttributes.addFlashAttribute("editErrorMessage", "Please Change Dataset Name");
            redirectAttributes.addAttribute("username", username);
            return "redirect:/datasets/{username}";
        }
        if (newName.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorIndex", index);
            redirectAttributes.addFlashAttribute("editErrorMessage", "Dataset Name Cannot Be Empty");
            redirectAttributes.addAttribute("username", username);
            return "redirect:/datasets/{username}";
        }
        try {
            success = datasetService.editDataset(id, newName, oldName, username);
        } catch (DatasetException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addAttribute("username", username);
            return "redirect:/datasets/{username}";
        }
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Dataset successfully edited");
        }
        return "redirect:/datasets/success";
    }

    @RequestMapping(value = "datasets/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        boolean success;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long datasetId = Long.parseLong(request.getParameter("fileUploadDatasetId"));
        String datasetName = request.getParameter("fileUploadDatasetName");
        if (!file.isEmpty()) {
            model.addAttribute("file", file);
        } else {
            String index = request.getParameter("fileUploadDatasetIndex");
            redirectAttributes.addFlashAttribute("noFileIndex", index);
            redirectAttributes.addFlashAttribute("noFileMessage", "Please Choose a File");
            redirectAttributes.addAttribute("username", username);
            return "redirect:/datasets/{username}";
        }
        success = dataFileService.createFile(file, datasetId, username, datasetName);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "File Successfully Uploaded");
        }
        return "redirect:/datasets/success";
    }

    @RequestMapping(value = "datasets/deleteFile", method = RequestMethod.POST)
    public String deleteFile(HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        boolean success = false;
        Long fileId = Long.parseLong(request.getParameter("fileId"));
        Long datasetId = Long.parseLong(request.getParameter("datasetId"));
        try {
            success = datasetService.deleteFileFromDataset(datasetId, fileId);
        } catch (DatasetException e) {
            redirectAttributes.addFlashAttribute("message", "Error: File could not be deleted");
        }
        if (success) {
            redirectAttributes.addFlashAttribute("message", "File Successfully Deleted");
        }
        return "redirect:/datasets/success";
    }

}

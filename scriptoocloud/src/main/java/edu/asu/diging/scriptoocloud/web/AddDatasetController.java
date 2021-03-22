package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.scriptoocloud.web.forms.DatasetEditForm;
import edu.asu.diging.scriptoocloud.web.forms.DatasetForm;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.security.Principal;
import java.util.Optional;

@Controller
@PropertySource("classpath:/config.properties")
public class AddDatasetController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${pageSize}")
    private int paginationSize;

    private final IDatasetService iDatasetService;
    private final IUserManager userManager;

    @Autowired
    public AddDatasetController(IDatasetService iDatasetService, IUserManager userManager) {
        this.iDatasetService = iDatasetService;
        this.userManager = userManager;
    }

    @RequestMapping(value = "auth/datasets/add", method = RequestMethod.POST)
    public String post(@Valid @ModelAttribute("dataset") DatasetForm datasetForm,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size,
                       Principal principal) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(paginationSize);
        String username = principal.getName();
        IUser user = userManager.findByUsername(username);
        datasetForm.setUsername(username);
        if (result.hasErrors()) {
            Page<Dataset> dbDatasets = iDatasetService
                    .findDatasets(PageRequest.of(currentPage - 1, pageSize), user);
            model.addAttribute("dbDatasets", dbDatasets);
            model.addAttribute("dataset", datasetForm);
            model.addAttribute("datasetEditForm", new DatasetEditForm());
            return "auth/datasets/list";
        }
        try {
            Dataset savedDataset = iDatasetService
                    .createDataset(datasetForm.getName(), user, "1",
                            datasetForm.getDescription());
            redirectAttributes
                    .addFlashAttribute("successMessage", "Dataset: " + savedDataset.getName()
                            + " successfully created");
        } catch (DatasetStorageException e) {
            Page<Dataset> dbDatasets = iDatasetService
                    .findDatasets(PageRequest.of(currentPage - 1, pageSize), user);
            model.addAttribute("dbDatasets", dbDatasets);
            model.addAttribute("dataset", datasetForm);
            model.addAttribute("datasetEditForm", new DatasetEditForm());
            model.addAttribute("errorMessage", "There was an error creating the dataset");
            logger.error("ERROR: could not create Dataset", e);
            return "auth/datasets/list";
        }
        return "redirect:/auth/datasets/list";
    }
}

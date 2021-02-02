package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.scriptoocloud.web.forms.DatasetEditForm;
import edu.asu.diging.scriptoocloud.web.forms.DatasetForm;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class EditDatasetController {

    @Value("${pageSize}")
    private int paginationSize;

    private final IDatasetService iDatasetService;
    private final IUserManager userManager;

    @Autowired
    public EditDatasetController(IDatasetService iDatasetService, IUserManager userManager) {
        this.iDatasetService = iDatasetService;
        this.userManager = userManager;
    }

    @RequestMapping(value = "auth/datasets/{id}/edit", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#datasetId, 'Dataset', 'edit')")
    public String edit(@PathVariable("id") Long datasetId,
                       @Valid @ModelAttribute("datasetEditForm") DatasetEditForm datasetEditForm,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size,
                       Principal principal) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(paginationSize);
        if (result.hasErrors()) {
            String username = principal.getName();
            IUser user = userManager.findByUsername(username);
            Page<Dataset> dbDatasets = iDatasetService.findDatasets(PageRequest.of(
                    currentPage - 1, pageSize), user);
            model.addAttribute("dbDatasets", dbDatasets);
            int totalPages = dbDatasets.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("dataset", new DatasetForm());
            model.addAttribute("datasetEditForm", datasetEditForm);
            model.addAttribute("errorId", datasetEditForm.getId());
            return "auth/datasets/list";
        } else {
            iDatasetService.editDataset(datasetId, datasetEditForm.getNewName());
        }
        redirectAttributes.addFlashAttribute("successMessage", "Dataset successfully edited");
        return "redirect:/auth/datasets/list";
    }
}

package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.scriptoocloud.web.forms.DatasetEditForm;
import edu.asu.diging.scriptoocloud.web.forms.DatasetForm;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@PropertySource("classpath:/config.properties")
public class ListDatasetsController {

    @Value("${pageSize}")
    private String paginationSize;

    private final IDatasetService iDatasetService;
    private final IUserManager userManager;

    @Autowired
    public ListDatasetsController(IDatasetService iDatasetService, IUserManager userManager) {
        this.iDatasetService = iDatasetService;
        this.userManager = userManager;
    }

    @RequestMapping(value = "auth/datasets/list", method = RequestMethod.GET)
    public String get(Model model,
                      @RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size,
                      Principal principal) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Integer.parseInt(paginationSize));
        String username = principal.getName();
        model.addAttribute("dataset", new DatasetForm());
        model.addAttribute("datasetEditForm", new DatasetEditForm());
        IUser user = userManager.findByUsername(username);
        Page<Dataset> dbDatasets = iDatasetService.findDatasets(PageRequest.of(currentPage - 1, pageSize), user);
        model.addAttribute("dbDatasets", dbDatasets);
        int totalPages = dbDatasets.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "auth/datasets/list";
    }
}

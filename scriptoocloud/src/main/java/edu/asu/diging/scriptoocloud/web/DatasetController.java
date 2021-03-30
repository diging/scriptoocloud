package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.service.IDataFileService;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@PropertySource("classpath:/config.properties")
public class DatasetController {

    @Value("${pageSize}")
    private String paginationSize;

    private final IDatasetService iDatasetService;
    private final IDataFileService iDataFileService;

    @Autowired
    public DatasetController(IDatasetService iDatasetService, IDataFileService iDataFileService) {
        this.iDatasetService = iDatasetService;
        this.iDataFileService = iDataFileService;
    }

    @RequestMapping(value = "auth/datasets/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'Dataset', 'create')")
    public String get(@PathVariable("id") Long id,
                      Model model,
                      @RequestParam("page") Optional<Integer> page,
                      @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Integer.parseInt(paginationSize));
        IDataset dataset = iDatasetService.findById(id);
        Page<DataFile> filesPage = iDataFileService.findFiles(PageRequest.of(currentPage - 1, pageSize), id);
        model.addAttribute("dataset", dataset);
        model.addAttribute("filesPage", filesPage);
        int totalPages = filesPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "auth/datasets/details";
    }
}

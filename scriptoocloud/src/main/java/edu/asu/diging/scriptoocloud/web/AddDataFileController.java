package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@PropertySource("classpath:/config.properties")
public class AddDataFileController {

    @Value("${pageSize}")
    private int paginationSize;

    private final IDataFileService dataFileService;
    private final IDatasetService datasetService;

    @Autowired
    public AddDataFileController(IDataFileService dataFileService, IDatasetService datasetService) {
        this.dataFileService = dataFileService;
        this.datasetService = datasetService;
    }

    @RequestMapping(value = "datasets/{id}/upload", method = RequestMethod.POST)
    // Make sure user owns Dataset in which files are uploaded
    @PreAuthorize("hasPermission(#datasetId, 'Dataset', 'edit')")
    public String uploadFile(@PathVariable("id") Long datasetId,
                             @RequestParam("file") MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes,
                             Principal principal,
                             Model model,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) {
        redirectAttributes.addAttribute("id", datasetId);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(paginationSize);
        String username = principal.getName();
        if (multipartFile.isEmpty()) {
            model.addAttribute("noFileMessage", "Please Choose a File");
            model.addAttribute("dataset", datasetService.findById(datasetId));
            Page<DataFile> filesPage = dataFileService.findFiles(PageRequest.of(currentPage - 1, pageSize), datasetId);
            model.addAttribute("filesPage", filesPage);
            int totalPages = filesPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            return "datasets/details";
        }
        try {
            byte[] bytes = multipartFile.getBytes();
            dataFileService.createFile(bytes, Long.toString(datasetId), username,
                    multipartFile.getOriginalFilename(), multipartFile.getContentType());
            redirectAttributes.addFlashAttribute("successMessage", "File Successfully Uploaded");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error reading the file");
        } catch (DataFileStorageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "There was an error saving the file");
        }
        return "redirect:/datasets/{id}";
    }
}

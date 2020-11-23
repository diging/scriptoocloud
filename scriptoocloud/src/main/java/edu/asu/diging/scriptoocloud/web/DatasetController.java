package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.service.IDataFileService;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class DatasetController {

    private final IDatasetService iDatasetService;
    private final IDataFileService iDataFileService;

    @Autowired
    public DatasetController(IDatasetService iDatasetService, IDataFileService iDataFileService) {
        this.iDatasetService = iDatasetService;
        this.iDataFileService = iDataFileService;
    }

    @RequestMapping(value = "datasets/{id}", method = RequestMethod.GET)
    public String get(@PathVariable("id") Long id, Model model) {
        IDataset dataset = iDatasetService.findById(id);
        Page<DataFile> files = iDataFileService.getFilesByDatasetId(id);
        model.addAttribute("dataset", dataset);
        model.addAttribute("files", files);
        return "datasets/details";
    }
}

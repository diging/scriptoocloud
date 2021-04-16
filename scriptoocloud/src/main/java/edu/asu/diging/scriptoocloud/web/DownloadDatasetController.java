package edu.asu.diging.scriptoocloud.web;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.scriptoocloud.core.service.IFileSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.Locale;

@Controller
public class DownloadDatasetController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IDatasetService iDatasetService;
    private final IFileSystemService iFileSystemService;

    @Autowired
    public DownloadDatasetController(IDatasetService iDatasetService, IFileSystemService iFileSystemService) {
        this.iDatasetService = iDatasetService;
        this.iFileSystemService = iFileSystemService;
    }

    @RequestMapping(value = "auth/datasets/{id}/download")
    @PreAuthorize("hasPermission(#datasetId, 'Dataset', 'download')")
    public void download(@PathVariable("id") Long datasetId,
                         HttpServletResponse response,
                         Principal principal) throws IOException, FileSystemStorageException {
        String username = principal.getName();
        String type = Dataset.class.getSimpleName().toLowerCase(Locale.ROOT);
        Dataset dataset = (Dataset) iDatasetService.findById(datasetId);
        if (dataset != null) {
            String filename = type + "_" + datasetId + "_version_" + dataset.getVersion() + ".zip";
            try {
                iFileSystemService.createZipFile(username, type, datasetId.toString(),
                        dataset.getVersion().toString());
                File file = new File(iFileSystemService.createPath(username, type,
                        datasetId.toString(),
                        dataset.getVersion().toString()).toString() + File.separator + filename);
                if (file.exists()) {
                    response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                    response.setContentType("application/zip");
                    OutputStream outputStream = response.getOutputStream();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    fileInputStream.close();
                    outputStream.close();
                    response.flushBuffer();

                    iFileSystemService.deleteDirectoryOrFile(
                            new File(iFileSystemService.createPath(username, type,
                                    datasetId.toString(),
                                    dataset.getVersion().toString()) + File.separator + filename));
                } else {
                    logger.error("ERROR: Could not find the zip file to download.");
                }
            } catch (FileSystemStorageException e) {
                logger.error("ERROR: There was a FileSystemStorageException when creating the zip file ", e);
            } catch (SecurityException e) {
                logger.error("ERROR: There was a SecurityException when accessing the zip file", e);
            } catch (IOException e) {
                logger.error("ERROR: There was an IOException when attempting to download the zip file ", e);
            }
        } else {
            logger.error("ERROR: Dataset could not be found.");
        }
    }
}


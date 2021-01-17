package edu.asu.diging.scriptoocloud.core.service.impl;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IFileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import edu.asu.diging.scriptoocloud.core.service.IDataFileService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Service class associated with user-uploaded DataFiles.  The class handles storing the
 * files on the file system (using the FileSystemService) and storing DataFile data in the database.
 */

@Transactional
@Service
public class DataFileService implements IDataFileService {

    private final DataFileRepository dataFileRepository;

    private final DatasetRepository datasetRepository;
    private final IFileSystemService fileSystemService;

    @Autowired
    public DataFileService(DataFileRepository dataFileRepository,
                           DatasetRepository datasetRepository,
                           IFileSystemService fileSystemService) {
        this.dataFileRepository = dataFileRepository;
        this.datasetRepository = datasetRepository;
        this.fileSystemService = fileSystemService;
    }

    /**
     * Creates a file on the filesystem and stores the DataFile
     * in the database. A Dataset has many DataFiles and a DataFile belongs to one Dataset.
     *
     * @param bytes     The byte array representation of a MultipartFile
     * @param datasetId The primary key of the Dataset as a String.
     * @param username  The user / owner of the Dataset
     * @param filename  The name of the file
     * @param type      The file type
     */
    @Override
    public void createFile(byte[] bytes, String datasetId, String username, String filename, String type) {
        try {
            fileSystemService.createFileInDirectory(username, datasetId, filename, bytes);
        } catch (DataFileStorageException e) {
            throw new DataFileStorageException("File could not be saved in the file system", e);
        }

        DataFile newFile = new DataFile();
        newFile.setName(filename);
        newFile.setType(type);
        newFile.setCreatedAt(OffsetDateTime.now());
        Optional<Dataset> dataset;
        try {
            dataset = datasetRepository.findById(Long.parseLong(datasetId));
        } catch (NumberFormatException e) {
            throw new DataFileStorageException("Dataset Id could not be parsed to a long", e);
        }
        if (dataset.isPresent()) {
            dataset.get().addFile(newFile);
            newFile.setDataset(dataset.get());
            datasetRepository.save(dataset.get());
            dataFileRepository.save(newFile);
        } else {
            throw new DataFileStorageException("Dataset not found");
        }
    }

    /**
     * Finds and returns a page of DataFiles
     *
     * @param pageable  The Pageable to calculate page size and current page
     * @param datasetId The Dataset id
     * @return The Page of DataFiles
     */
    @Override
    public Page<DataFile> findPaginatedFiles(Pageable pageable, Long datasetId) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<DataFile> files = dataFileRepository.findAllByDataset_Id(datasetId);

        List<DataFile> list;

        if (files.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, files.size());
            list = files.subList(startItem, toIndex);
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), files.size());
    }
}

package edu.asu.diging.scriptoocloud.core.service.impl;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IFileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import edu.asu.diging.scriptoocloud.core.service.IDataFileService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Service class associated with user-uploaded DataFiles.  The class handles storing the
 * files on the file system (using the FileSystemService) and storing DataFile data in the database.
 *
 * @author John Coronite
 */

@Transactional(rollbackFor = Exception.class)
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

    @Override
    public DataFile createFile(byte[] bytes, String datasetId, String username, String filename,
                               String type) throws DataFileStorageException {

        DataFile newFile = new DataFile();
        newFile.setName(filename);
        newFile.setExtension(type);
        newFile.setCreatedAt(OffsetDateTime.now());
        Optional<Dataset> dataset;
        String version;
        try {
            dataset = datasetRepository.findById(Long.parseLong(datasetId));
        } catch (NumberFormatException e) {
            throw new DataFileStorageException("Dataset Id could not be parsed to a long", e);
        }
        if (dataset.isPresent()) {

            version = dataset.get().getVersion() != null ? dataset.get().getVersion().toString() : null;
            newFile.setDataset(dataset.get());
            newFile = dataFileRepository.save(newFile);
            dataset.get().addFile(newFile);
            datasetRepository.save(dataset.get());
        } else {
            throw new DataFileStorageException("Dataset not found");
        }

        // Save file with unique name (its id) and maintain the file type extension
        String indexBasedFilename = getIndexBasedFileName(newFile);

        try {
            fileSystemService.createFileInDirectory(username,
                    Dataset.class.getSimpleName().toLowerCase(Locale.ROOT), datasetId, version,
                    indexBasedFilename, bytes);
        } catch (FileSystemStorageException e) {
            throw new DataFileStorageException("File could not be saved in the file system", e);
        }
        return newFile;
    }

    @Override
    public Page<DataFile> findFiles(Pageable pageable, Long datasetId) {
        return dataFileRepository.findAllByDatasetId(datasetId, pageable);
    }

    @Override
    public String getIndexBasedFileName(DataFile datafile) {
        String filename = datafile.getName();
        return datafile.getId() + filename.substring(filename.lastIndexOf("."));
    }
}

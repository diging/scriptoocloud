package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DataFileNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.scriptoocloud.core.service.IDataFileService;
import edu.asu.diging.scriptoocloud.core.service.IDatasetService;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;

/**
 * The Service class associated with user Datasets.  The class handles the association between
 * Datasets and DataFiles they contain.
 *
 * @author John Coronite
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class DatasetService implements IDatasetService {

    private final String DIR_NAME = Dataset.class.getSimpleName().toLowerCase(Locale.ROOT);

    private final FileSystemService fileSystemService;
    private final IDataFileService dataFileService;
    private final DatasetRepository datasetRepository;
    private final DataFileRepository dataFileRepository;

    @Autowired
    public DatasetService(FileSystemService fileSystemService,
                          IDataFileService dataFileService,
                          DatasetRepository datasetRepository,
                          DataFileRepository dataFileRepository) {
        this.fileSystemService = fileSystemService;
        this.dataFileService = dataFileService;
        this.datasetRepository = datasetRepository;
        this.dataFileRepository = dataFileRepository;
    }

    @Override
    public Dataset createDataset(String name, IUser user, Long version, String description)
            throws DatasetStorageException {

        Dataset dataset = new Dataset();
        dataset.setName(name);
        dataset.setUser(user);
        dataset.setDescription(description);
        dataset.setCreationDate(ZonedDateTime.now());
        dataset.setVersion(version);
        Dataset savedDataset = datasetRepository.save(dataset);

        // create directories on the file system (using Dataset id)
        try {
            fileSystemService.addDirectories(user.getUsername(), DIR_NAME,
                    String.valueOf(savedDataset.getId()), version.toString());
        } catch (FileSystemStorageException e) {
            throw new DatasetStorageException("An IOException prevented the Dataset from being created", e);
        }
        return savedDataset;
    }

    @Override
    public void editDataset(Long id, String newName) {

        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (dataset.isPresent()) {
            dataset.get().setName(newName);
            datasetRepository.save(dataset.get());
        }
    }

    @Override
    public void deleteDataset(Long id, String username) throws DatasetStorageException {

        // Delete Dataset and its files from the database
        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (dataset.isPresent()) {
            dataFileRepository.deleteAllByDatasetId(id);
        }
        datasetRepository.deleteById(id);

        // Then delete Dataset Directories and files from the filesystem
        try {
            fileSystemService.deleteDirectories(username, DIR_NAME, Long.toString(id));
        } catch (FileSystemStorageException e) {
            throw new DatasetStorageException("An invalid path prevented Dataset deletion", e);
        } catch (SecurityException e) {
            throw new DatasetStorageException("Read access to directory was denied", e);
        }
    }

    @Override
    public IDataset findById(Long id) {
        return datasetRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Dataset> findDatasets(Pageable pageable, IUser user) {
        return datasetRepository.findAllByUser(user, pageable);
    }

    @Override
    public Page<Dataset> findAll(Pageable pageable) {
        return datasetRepository.findAll(pageable);
    }

    @Override
    public boolean deleteFileFromDataset(Long datasetId, Long fileId) throws DatasetStorageException,
            DatasetNotFoundException, DataFileNotFoundException {

        Optional<Dataset> dataset = datasetRepository.findById(datasetId);
        Optional<DataFile> dataFile = dataFileRepository.findById(fileId);
        if (!dataset.isPresent()) {
            throw new DatasetNotFoundException("Dataset not found in the database");
        }
        if (!dataFile.isPresent()) {
            throw new DataFileNotFoundException("DataFile not found in the database");
        }
        String username = dataset.get().getUsername();
        String version = dataset.get().getVersion() != null ? dataset.get().getVersion().toString() : null;

        // Remove join from dataset
        dataset.get().getFiles().remove(dataFile.get());
        datasetRepository.save(dataset.get());

        // delete file from database
        dataFileRepository.deleteById(fileId);

        try {
            // Removes file from file system
            String indexBasedFilename = dataFileService.getIndexBasedFileName(dataFile.get());
            String pathString = fileSystemService.createPath(username, DIR_NAME,
                    String.valueOf(datasetId), version).toString();
            File fileToBeDeleted = Paths.get(pathString, indexBasedFilename).toFile();
            return fileSystemService.deleteDirectoryOrFile(fileToBeDeleted);
        } catch (FileSystemStorageException e) {
            throw new DatasetStorageException("Path to file could not be found", e);
        } catch (UnsupportedOperationException e) {
            throw new DatasetStorageException("Could not convert path to File", e);
        } catch (SecurityException e) {
            throw new DatasetStorageException("File was created, but could not be deleted", e);
        }
    }

    @Override
    public Integer getSize(Long datasetId) {
        Optional<Dataset> dataset = datasetRepository.findById(datasetId);
        if (dataset.isPresent()) {
            return (dataset.get().getFiles() == null) ? 0 : dataset.get().getFiles().size();
        }
        return null;
    }
}

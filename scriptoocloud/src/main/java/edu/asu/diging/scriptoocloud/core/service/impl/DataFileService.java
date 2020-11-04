package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.OffsetDateTime;
import java.util.Optional;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DataFileException;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import edu.asu.diging.scriptoocloud.core.service.IDataFileService;

/**
 * The Service class associated with user-uploaded DataFiles.  The class handles storing the
 * files on the file system and storing DataFile data in the database.
 */

@Service
public class DataFileService implements IDataFileService {

    private final Path rootLocation;
    private final DataFileRepository dataFileRepository;
    private final DatasetRepository datasetRepository;

    @Autowired
    public DataFileService(StorageProperties properties,
                           DataFileRepository dataFileRepository,
                           DatasetRepository datasetRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.dataFileRepository = dataFileRepository;
        this.datasetRepository = datasetRepository;
    }

    /**
     * Creates a file on the filesystem and, if successful, stores the DataFile
     * data in the database. A Dataset has many DataFiles and a DataFile belongs to one Dataset.
     *
     * @param file        A MultipartFile.
     * @param datasetId   The primary key of the Dataset.
     * @param username    The user / owner of the Dataset.
     * @param datasetName The name of the Dataset.
     * @return boolean indicating success.
     */
    @Override
    public boolean createFile(MultipartFile file, Long datasetId, String username, String datasetName) {
        boolean success;
        String fileName = file.getOriginalFilename();
        String type = file.getContentType();

        try {
            success = createFileInDirectory(username, datasetName, file);
        } catch (DataFileException e) {
            throw new DataFileException("File could not be saved in the file system", e);
        }

        // Only store file in database if saving file on filesystem was successful
        if (success) {
            Optional<DataFile> dataFile = dataFileRepository.findByName(fileName);
            if (dataFile.isPresent() &&
                    dataFile.get().getDataset().getId().equals(datasetId) &&
                    dataFile.get().getType().equals(type)) {
                // if a file with the same name & type already exists in the dataset, update time stamp
                Long dataFileId = dataFile.get().getId();
                editFile(dataFileId);
            } else {
                Optional<Dataset> dataset = datasetRepository.findById(datasetId);
                if (dataset.isPresent()) {
                    DataFile newFile = new DataFile();
                    newFile.setDataset(dataset.get());
                    newFile.setName(fileName);
                    newFile.setType(type);
                    newFile.setCreatedAt(OffsetDateTime.now());
                    dataset.get().addFile(newFile);
                    dataFileRepository.save(newFile);
                }
            }
        }
        return success;
    }

    /**
     * Edits DataFiles in the database (occurs when users upload files in the same dataset with the
     * same name and type). In this case, the only database entry which needs to be updated is the
     * createdAt field.
     *
     * @param id The id of the DataFile to be updated
     */
    @Override
    public void editFile(Long id) {
        Optional<DataFile> datafile = dataFileRepository.findById(id);
        datafile.ifPresent(dataFile -> dataFile.setCreatedAt(OffsetDateTime.now()));
    }


    /**
     * Stores the user-uploaded file on the file system.
     *
     * @param username    The name (primary key) of the user / owner of the Dataset to which
     *                    the file belongs.
     * @param datasetName The name of the Dataset to which the file belongs.
     * @param file        The MultipartFile to be stored.
     * @return boolean indicating success.
     * @throws DataFileException If the file could not be stored on the file system.
     */
    private boolean createFileInDirectory(String username, String datasetName, MultipartFile file)
            throws DataFileException {
        InputStream inputStream = null;

        if (file.isEmpty()) {
            throw new DataFileException("Failed to store empty file.");
        }
        if (username == null || datasetName == null) {
            throw new DataFileException(
                    "Cannot create file path with null values for username or dataSet name");
        }
        Path destinationFile = this.rootLocation.resolve(
                Paths.get(username, datasetName, file.getOriginalFilename()))
                .normalize().toAbsolutePath();
        // make sure path structure is:
        // /serverRoot/username/datasetName/file
        if (!destinationFile.getParent().getParent().getParent()
                .equals(this.rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new DataFileException("Cannot store file at calculated location");
        }
        try {
            inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ex) {
                throw new DataFileException(
                        "File could not be copied to its directory and InputStream could not be closed", ex);
            }
            throw new DataFileException(
                    "An IO Error occurred while copying the file to its directory", e);
        } catch (SecurityException e) {
            throw new DataFileException(
                    "A Security Exception occurred while copying the file to its directory", e);
        }
    }
}

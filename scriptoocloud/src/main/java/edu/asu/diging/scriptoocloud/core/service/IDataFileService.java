package edu.asu.diging.scriptoocloud.core.service;

import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface associated with user-uploaded DataFiles. Instances of this class create and
 * retrieve DataFiles.
 */
public interface IDataFileService {

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
    DataFile createFile(byte[] bytes, String datasetId, String username, String filename,
                        String type) throws DataFileStorageException;

    /**
     * Finds and returns a page of DataFiles
     *
     * @param pageable  The Pageable to display only DataFiles for requested page
     * @param datasetId The Dataset id
     * @return The Page of DataFiles
     */
    Page<DataFile> findFiles(Pageable pageable, Long datasetId);
}

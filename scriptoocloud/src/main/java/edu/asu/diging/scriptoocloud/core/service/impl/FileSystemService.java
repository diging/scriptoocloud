package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.service.IFileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The Service class which handles adding and deleting files on the server file system.
 *
 * @author John Coronite
 */
@Service
@PropertySource("classpath:/config.properties")
public class FileSystemService implements IFileSystemService {

    @Value("${rootUploadLocation}")
    private String rootLocationString;

    @Autowired
    public FileSystemService() {
    }

    public String getRootLocationString() {
        return rootLocationString != null ? rootLocationString : "";
    }

    @Override
    public boolean addDirectories(String username, String type, String id, String version) throws
            FileSystemStorageException {
        if (username == null) {
            return false;
        }
        Path path = createPath(username, type, id, version);
        if (path == null) {
            return false;
        }
        File directory = path.toFile();
        // if the Path was created successfully, create the directory
        try {
            if (!directory.exists()) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new FileSystemStorageException("IOException occurred when attempting to add the Dataset", e);
        } catch (UnsupportedOperationException e) {
            throw new FileSystemStorageException("UnsupportedOperationException occurred when attempting to add the Dataset", e);
        } catch (SecurityException e) {
            throw new FileSystemStorageException("SecurityException occurred when attempting to add the Dataset", e);
        }
        return true;
    }

    @Override
    public Path createPath(String username, String type, String id, String version) throws
            FileSystemStorageException {
        Path path;
        try {
            if (username != null && (type == null && id == null && version == null)) {
                path = Paths.get(getRootLocationString(), username);
            } else if (username != null && (type != null && id != null && version != null)) {
                path = Paths.get(getRootLocationString(), username, type, id, version);
            } else if (username != null && type != null && id != null) {
                path = Paths.get(getRootLocationString(), username, type, id);
            } else {
                return null;
            }
            return Paths.get(StringUtils.cleanPath(Objects.requireNonNull(path).toString()));
        } catch (InvalidPathException e) {
            throw new FileSystemStorageException("Path could not be created");
        }
    }

    @Override
    public void deleteDirectories(String username, String type, String id)
            throws FileSystemStorageException {

        try {
            Path path = createPath(username, type, id, null);
            File directory = path.toFile();
            deleteDirectoryOrFile(directory);
        } catch (InvalidPathException e) {
            throw new FileSystemStorageException(
                    "The path could not be found when attempting to delete the Dataset from the file system", e);
        } catch (SecurityException e) {
            throw new FileSystemStorageException(
                    "A SecurityException occurred when attempting to delete the Dataset from the file system", e);
        }
    }

    @Override
    public boolean deleteDirectoryOrFile(File directoryToBeDeleted) throws
            FileSystemStorageException {

        File[] allContents;
        try {
            allContents = directoryToBeDeleted.listFiles();
        } catch (SecurityException e) {
            throw new FileSystemStorageException(
                    "A SecurityException occurred when generating list of files to be deleted", e);
        }
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectoryOrFile(file);
            }
        }
        try {
            return directoryToBeDeleted.delete();
        } catch (SecurityException e) {
            throw new FileSystemStorageException(
                    "A SecurityException occurred when deleting a directory", e);
        }
    }

    @Override
    public void createFileInDirectory(String username, String type, String id, String version,
                                      String filename, byte[] bytes) throws FileSystemStorageException {
        Path destinationFile;
        if (version == null) {
            destinationFile = Paths.get(getRootLocationString()).resolve(
                    Paths.get(username, type, id, filename))
                    .normalize().toAbsolutePath();
        } else {
            destinationFile = Paths.get(getRootLocationString()).resolve(
                    Paths.get(username, type, id, version, filename))
                    .normalize().toAbsolutePath();
        }
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileSystemStorageException(
                    "An IO Error occurred while copying the file to its directory", e);
        } catch (SecurityException e) {
            throw new FileSystemStorageException(
                    "A Security Exception occurred while copying the file to its directory", e);
        }
    }

    @Override
    public void createZipFile(String username, String type, String id, String version)
            throws FileSystemStorageException, IOException {

        Path pathOfZip = createPath(username, type, id, version);
        if (pathOfZip != null) {
            String directoryOfZip = pathOfZip.toString();
            String zipFileName = directoryOfZip + File.separator + type + "_" + id + "_version_" +
                    version + ".zip";
            FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            File fileToZip = new File(directoryOfZip);

            File[] children = fileToZip.listFiles();
            assert children != null;
            for (File childFile : children) {
                if (!zipFileName.contains(childFile.getName()) && !childFile.isHidden()) {
                    FileInputStream fileInputStream = new FileInputStream(childFile);
                    ZipEntry zipEntry = new ZipEntry(childFile.getName());
                    zipOutputStream.putNextEntry(zipEntry);

                    byte[] bytes = new byte[4096];
                    int length;
                    while ((length = fileInputStream.read(bytes)) >= 0) {
                        zipOutputStream.write(bytes, 0, length);
                    }
                    fileInputStream.close();
                }
            }
            zipOutputStream.close();
            fileOutputStream.close();
        }
    }
}
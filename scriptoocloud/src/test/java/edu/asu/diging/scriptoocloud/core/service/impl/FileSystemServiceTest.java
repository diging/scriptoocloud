package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author Jason Ormsby
 * @author John Coronite
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AnnotationConfigContextLoader.class)
class FileSystemServiceTest {

    private final String VERSION = "1";

    private final String fileName = "filename.txt";

    private final byte[] bytes = "abc".getBytes();

    @InjectMocks
    private FileSystemService fileSystemService;

    @TempDir
    protected Path path;

    private String username;

    private String type;

    private String id;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        initPathSegments();
    }

    private void initPathSegments() {
        String pathString = path.toString();
        String[] segments = pathString.split(File.separator);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < segments.length - 2; i++) {
            stringBuilder.append(segments[i]);
            if (i != segments.length - 2)
                stringBuilder.append(File.separator);
        }
        username = stringBuilder.toString();
        type = segments[segments.length - 2];
        id = segments[segments.length - 1];
    }

    @Test
    public void test_addDirectories_success() throws FileSystemStorageException, IOException {
        fileSystemService.addDirectories(username, type, id, VERSION);
        File file = Paths.get(path + VERSION).toFile();
        Assertions.assertTrue(file.createNewFile());
        fileSystemService.deleteDirectoryOrFile(file);
        Assertions.assertFalse(file.exists());
    }

    @Test
    public void test_addDirectories_failed() throws FileSystemStorageException {
        Assertions.assertFalse(fileSystemService
                .addDirectories(null, null, null, null));
        Assertions.assertFalse(fileSystemService
                .addDirectories(null, "foo", "bar", "baz"));
        Assertions.assertFalse(fileSystemService
                .addDirectories(null, null, "bar", "baz"));
        Assertions.assertFalse(fileSystemService
                .addDirectories(null, "foo", null, "baz"));
        Assertions.assertFalse(fileSystemService
                .addDirectories(null, "foo", "bar", null));

        Assertions.assertFalse(fileSystemService
                .addDirectories("username", "foo", null, null));
        Assertions.assertFalse(fileSystemService
                .addDirectories("username", null, "bar", null));
        Assertions.assertFalse(fileSystemService
                .addDirectories("username", null, null, "baz"));
    }

    @Test
    public void test_createPath_usernameTypeIdVersionSuccess() throws FileSystemStorageException {
        Path createdPath = path.resolve(VERSION);
        Assertions.assertTrue(createdPath
                .endsWith(fileSystemService.createPath(username, type, id, VERSION)));
    }


    @Test
    public void test_createPath_usernameSuccess() throws FileSystemStorageException {
        Path createdPath = Paths.get(username);
        Assertions.assertTrue(createdPath.endsWith(fileSystemService.createPath(username, null,
                null, null)));
    }

    @Test
    public void test_createPath_usernameTypeIdSuccess() throws FileSystemStorageException {
        Path createdPath = Paths.get(username, type, id);
        Assertions.assertTrue(createdPath.endsWith(fileSystemService.createPath(username, type,
                id, null)));
    }

    @Test
    public void test_createPath_nullType() throws FileSystemStorageException {
        Assertions.assertNull(fileSystemService.createPath(username, null, id, VERSION));
    }

    @Test
    public void test_createPath_nullUsername() throws FileSystemStorageException {
        Assertions.assertNull(fileSystemService.createPath(null, "dataset", id, VERSION));
    }

    @Test
    public void test_deleteDirectories_success() throws FileSystemStorageException, IOException {
        File file = new File(path + File.separator + VERSION);
        Assertions.assertTrue(file.createNewFile());
        fileSystemService.deleteDirectories(username, type, id);
        Assertions.assertFalse(file.exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_deleteFile() throws IOException, FileSystemStorageException {
        File file = new File(path + "myfile");
        Assertions.assertTrue(file.createNewFile());
        fileSystemService.deleteDirectoryOrFile(file);
        Assertions.assertFalse(file.exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_deleteFolderAndContents() throws IOException, FileSystemStorageException {
        Files.write(path.resolve("myfile"), "abc".getBytes());
        File file = path.resolve("myfile").toFile();

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(path.toFile().exists());

        fileSystemService.deleteDirectoryOrFile(path.toFile());

        Assertions.assertFalse(file.exists());
        Assertions.assertFalse(path.toFile().exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_noSuchFile() {
        File file = new File("NotOnDrive");
        Assertions.assertFalse(file.exists());
        Assertions.assertDoesNotThrow(() -> fileSystemService.deleteDirectoryOrFile(file));
    }

    @Test
    public void test_createFileInDirectory_version() throws FileSystemStorageException {
        fileSystemService.addDirectories(username, type, id, VERSION);
        fileSystemService.createFileInDirectory(username, type, id, VERSION, fileName, bytes);
        Assertions.assertTrue(Files.exists(path.resolve(VERSION + File.separator + fileName)));
    }

    @Test
    public void test_createFileInDirectory_nullVersion() throws FileSystemStorageException {
        fileSystemService.addDirectories(username, type, id, null);
        fileSystemService.createFileInDirectory(username, type, id, null, fileName, bytes);
        Assertions.assertTrue(Files.exists(path.resolve(fileName)));
    }

    @Test
    public void test_createFileInDirectory_failed() {
        Assertions.assertThrows(FileSystemStorageException.class, () -> fileSystemService
                .createFileInDirectory(username, type, id, VERSION, fileName, bytes));
    }
}

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

    private static final String ID = "1";

    @InjectMocks
    private FileSystemService fileSystemService;

    @TempDir
    protected Path path;

    private String username;

    private String type;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        initPathSegments();
    }

    private void initPathSegments() {
        String pathString = path.toString();
        String[] segments = pathString.split(File.separator);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < segments.length - 1; i++) {
            stringBuilder.append(segments[i]);
            if (i != segments.length - 2)
                stringBuilder.append(File.separator);
        }
        username = stringBuilder.toString();
        type = segments[segments.length - 1];
    }

    @Test
    public void test_addDirectories() throws FileSystemStorageException, IOException {
        fileSystemService.addDirectories(username, type, ID);
        File file = Paths.get(path + ID).toFile();
        Assertions.assertTrue(file.createNewFile());
        fileSystemService.deleteDirectoryOrFile(file);
        Assertions.assertFalse(file.exists());
    }

    @Test
    public void test_addDirectories_failed() {
        Assertions.assertThrows(NullPointerException.class,
                () -> fileSystemService.addDirectories(null, null, null));
    }

    @Test
    public void test_createPath() throws FileSystemStorageException {
        Path createdPath = path.resolve(ID);
        Assertions.assertTrue(createdPath.endsWith(fileSystemService.createPath(username, type, ID)));
    }

    @Test
    public void test_createPath_nullId() throws FileSystemStorageException {
        Assertions.assertTrue(path.endsWith(fileSystemService.createPath(username, type, null)));
    }

    @Test
    public void test_createPath_failed() {
        Assertions.assertThrows(NullPointerException.class,
                () -> fileSystemService.createPath(null, null, null));
    }

    @Test
    public void test_deleteDirectories() throws FileSystemStorageException, IOException {
        File file = new File(path + File.separator + ID);
        Assertions.assertTrue(file.createNewFile());
        fileSystemService.deleteDirectories(username, type, ID);
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
}

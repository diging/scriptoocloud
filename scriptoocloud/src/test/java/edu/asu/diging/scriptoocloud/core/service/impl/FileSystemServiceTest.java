package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Jason Ormsby
 * @author John Coronite
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FileSystemService.class)
class FileSystemServiceTest {

    private final String username = "username";
    private final String type = "type";
    private final String id = "1";

    @Mock
    private final FileSystemService fileSystemService;

    @Autowired
    public FileSystemServiceTest(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @TempDir
    protected Path folder;

    @Test
    public void test_addDirectories() {
        folder = folder.resolve(username + "/" + type + "/" + id);
        FileSystemService spyFileSystemService = Mockito.spy(fileSystemService);
        Mockito.when(spyFileSystemService.createPath(username, type, id)).thenReturn(folder);
        Assertions.assertDoesNotThrow(() -> spyFileSystemService.addDirectories(username, type, id));
    }

    @Test
    public void test_CreatePath() {
        String username = "username";
        String type = "type";
        String id = "1";
        Path path = fileSystemService.createPath(username, type, id);
        Assertions.assertEquals(path.toString(), fileSystemService.getRootLocationString() +
                "/username/type/1");
    }

    @Test
    public void test_CreatePath_nullId() {
        String username = "username";
        String type = "type";
        Path path = fileSystemService.createPath(username, type, null);
        Assertions.assertEquals(path.toString(), fileSystemService.getRootLocationString() +
                "/username/type");
    }

    @Test
    public void test_deleteDirectories() throws FileSystemStorageException, IOException {
        File file = new File(folder + File.separator + id);
        String pathString = file.getPath();
        // ReflectionUtils doesn't seem to allow for creating more than one directory or file
        // relative to the temporary directory - infer a username and type from existing path
        String[] segments = pathString.split(File.separator);
        String segmentUserName = segments[segments.length - 3];
        String segmentType = segments[segments.length - 2];
        Assertions.assertTrue(file.createNewFile());
        FileSystemService spyFileSystemService = Mockito.spy(fileSystemService);
        Mockito.when(spyFileSystemService.createPath(segmentUserName, segmentType, id)).thenReturn(file.toPath());
        spyFileSystemService.deleteDirectories(segmentUserName, segmentType, id);
        Assertions.assertFalse(file.exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_deleteFile() throws IOException, FileSystemStorageException {
        File file = new File(folder + "myfile");
        Assertions.assertTrue(file.createNewFile());
        fileSystemService.deleteDirectoryOrFile(file);
        Assertions.assertFalse(file.exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_deleteFolderAndContents() throws IOException, FileSystemStorageException {
        Files.write(folder.resolve("myfile"), "abc".getBytes());
        File file = folder.resolve("myfile").toFile();

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(folder.toFile().exists());

        fileSystemService.deleteDirectoryOrFile(folder.toFile());

        Assertions.assertFalse(file.exists());
        Assertions.assertFalse(folder.toFile().exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_noSuchFile() {
        File file = new File("NotOnDrive");

        Assertions.assertFalse(file.exists());
        Assertions.assertDoesNotThrow(() -> fileSystemService.deleteDirectoryOrFile(file));
    }
}

package edu.asu.diging.scriptoocloud.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/*
 *
 *
 * @author Jason Ormsby
 */

class FileSystemServiceTest {

    /*
     * New temporary directory generated for every test
     */
    @TempDir
    protected Path folder;

    @InjectMocks
    private FileSystemService serviceToTest;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_deleteDirectoryOrFile_deleteFile() throws IOException, FileSystemStorageException {
        File file = new File(folder + "myfile");
        file.createNewFile();

        Assertions.assertEquals(true, file.exists());

        serviceToTest.deleteDirectoryOrFile(file);

        Assertions.assertEquals(false, file.exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_deleteFolderAndContents() throws IOException, FileSystemStorageException {
        Files.write(folder.resolve("myfile"), "abc".getBytes());
        File file = folder.resolve("myfile").toFile();

        Assertions.assertEquals(true, file.exists());
        Assertions.assertEquals(true, folder.toFile().exists());

        serviceToTest.deleteDirectoryOrFile(folder.toFile());

        Assertions.assertEquals(false, file.exists());
        Assertions.assertEquals(false, folder.toFile().exists());
    }

    @Test
    public void test_deleteDirectoryOrFile_noSuchFile() throws IOException {
        File file = new File("NotOnDrive");

        Assertions.assertEquals(false, file.exists());
        Assertions.assertDoesNotThrow(() -> serviceToTest.deleteDirectoryOrFile(file));
    }
}

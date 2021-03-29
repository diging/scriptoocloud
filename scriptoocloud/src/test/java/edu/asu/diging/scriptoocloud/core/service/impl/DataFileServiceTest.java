package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DataFileStorageException;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.model.IDataFile;
import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Test class for DataFileService
 *
 * @author John Coronite
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AnnotationConfigContextLoader.class)
class DataFileServiceTest {

    @InjectMocks
    private DataFileService dataFileService;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private DatasetRepository datasetRepository;

    @Mock
    private FileSystemService fileSystemService;

    private final Long DATASET_ID = 100L;

    private final String DATASET_ID_STRING = "100";

    private final String DATASET_VERSION_STRING = "1";

    private final int PAGE_SIZE = 25;

    private final int SECOND_PAGE_SIZE = 10;

    private final int PAGE_NUMBER = 1;

    private final String USERNAME = "username";

    private final String FILE_NAME = "fileName.txt";

    private final String FILE_EXTENSION = "txt";

    private final String FILE_TYPE = Dataset.class.getSimpleName().toLowerCase(Locale.ROOT);

    private final String INDEX_BASED_FILENAME = "2.txt";

    private final byte[] BYTES = "abc".getBytes();

    private final OffsetDateTime CREATED_AT = OffsetDateTime.now();

    private IDataset dataset;

    private IDataFile dataFile;

    private IUser user;

    private List<DataFile> filePage;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        initUser();
        initDataFile();
        initDataset();
        initFilePage();
    }

    private void initUser() {
        user = new SimpleUser();
        user.setUsername(USERNAME);
    }

    private void initDataFile() {
        dataFile = new DataFile();
        dataFile.setCreatedAt(CREATED_AT);
        dataFile.setName(FILE_NAME);
        dataFile.setExtension(FILE_EXTENSION);
        Long DATAFILE_ID = 2L;
        dataFile.setId(DATAFILE_ID);
    }

    private void initDataset() {
        dataset = new Dataset();
        dataset.setUser(user);
        String DATASET_NAME = "datasetName";
        dataset.setName(DATASET_NAME);
        dataset.setId(DATASET_ID);
        Long DATASET_VERSION = 1L;
        dataset.setVersion(DATASET_VERSION);
        dataset.setCreationDate(ZonedDateTime.now());
    }

    private void initFilePage() {
        filePage = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE + SECOND_PAGE_SIZE; i++) {
            DataFile dataFile = new DataFile();
            filePage.add(dataFile);
        }
    }

    @Test
    public void test_createFile_success() throws FileSystemStorageException {
        dataFile.setDataset((Dataset) dataset);
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.save((DataFile) Mockito.argThat(
                new DataFileArgMatcher(dataFile)))).thenReturn((DataFile) dataFile);
        Assertions.assertDoesNotThrow(() -> dataFileService.createFile(BYTES, DATASET_ID_STRING,
                USERNAME, FILE_NAME, FILE_EXTENSION));
        Mockito.verify(fileSystemService).createFileInDirectory(USERNAME, FILE_TYPE, DATASET_ID_STRING,
                DATASET_VERSION_STRING, INDEX_BASED_FILENAME, BYTES);
    }

    @Test
    public void test_createFile_failedDataFileException() {
        Mockito.when(datasetRepository.findById(DATASET_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataFileStorageException.class, () -> dataFileService.createFile(
                BYTES, DATASET_ID_STRING, USERNAME, FILE_NAME, FILE_EXTENSION));
    }

    @Test
    public void test_createFile_failedFileSystemException() throws FileSystemStorageException {
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.save((DataFile) Mockito.argThat(
                new DataFileArgMatcher(dataFile)))).thenReturn((DataFile) dataFile);
        dataFile.setDataset((Dataset) dataset);
        Mockito.doThrow(new FileSystemStorageException("Error")).when(fileSystemService)
                .createFileInDirectory(USERNAME, FILE_TYPE, DATASET_ID_STRING,
                        DATASET_VERSION_STRING, INDEX_BASED_FILENAME, BYTES);
        Assertions.assertThrows(DataFileStorageException.class, () -> dataFileService.createFile(
                BYTES, DATASET_ID_STRING, USERNAME, FILE_NAME, FILE_EXTENSION));
    }

    @Test
    public void test_createFile_datasetWithNullVersion() throws FileSystemStorageException {
        dataset.setVersion(null);
        dataFile.setDataset((Dataset) dataset);
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.save((DataFile) Mockito.argThat(
                new DataFileArgMatcher(dataFile)))).thenReturn((DataFile) dataFile);
        Assertions.assertDoesNotThrow(() -> dataFileService.createFile(BYTES, DATASET_ID_STRING,
                USERNAME, FILE_NAME, FILE_EXTENSION));
        Mockito.verify(fileSystemService).createFileInDirectory(USERNAME, FILE_TYPE, DATASET_ID_STRING,
                null, INDEX_BASED_FILENAME, BYTES);
    }

    @Test
    public void test_findFiles_success() {
        List<DataFile> files = new ArrayList<>();
        files.add((DataFile) dataFile);
        Page<DataFile> fileList = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Mockito.when(dataFileRepository.findAllByDatasetId(DATASET_ID,
                pageRequest)).thenReturn(fileList);
        Assertions.assertEquals(fileList.getTotalElements(), 1);
        Assertions.assertEquals(fileList, dataFileService.findFiles(pageRequest, DATASET_ID));
    }

    @Test
    public void test_findFiles_empty() {
        List<DataFile> files = new ArrayList<>();
        Page<DataFile> fileList = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Mockito.when(dataFileRepository.findAllByDatasetId(DATASET_ID,
                pageRequest)).thenReturn(fileList);
        Assertions.assertTrue(fileList.isEmpty());
        Assertions.assertEquals(fileList, dataFileService.findFiles(pageRequest, DATASET_ID));
    }

    @Test
    public void test_findFiles_secondPage() {
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER + 1, PAGE_SIZE);
        Page<DataFile> dataFileListSecondPage = new PageImpl<>(filePage.subList(PAGE_SIZE - 1,
                (PAGE_SIZE - 1 + SECOND_PAGE_SIZE)));
        Mockito.when(dataFileRepository.findAllByDatasetId(DATASET_ID,
                pageRequest)).thenReturn(dataFileListSecondPage);
        Assertions.assertEquals(dataFileListSecondPage, dataFileService.findFiles(pageRequest,
                DATASET_ID));
        Assertions.assertEquals(SECOND_PAGE_SIZE, dataFileListSecondPage.getTotalElements());
    }

    @Test
    public void test_getIndexBasedFileName_success() {
        Assertions.assertEquals(INDEX_BASED_FILENAME, dataFileService
                .getIndexBasedFileName((DataFile) dataFile));
    }

    @Test
    public void test_getIndexBasedFileName_failure() {
        dataFile.setName("foobar.txt");
        dataFile.setId(3L);
        Assertions.assertNotEquals(INDEX_BASED_FILENAME, dataFileService
                .getIndexBasedFileName((DataFile) dataFile));
    }

    class DataFileArgMatcher extends ArgumentMatcher<IDataFile> {

        private final IDataFile dataFileToBeTested;

        public DataFileArgMatcher(IDataFile dataFile) {
            dataFileToBeTested = dataFile;
        }

        @Override
        public boolean matches(Object arg) {
            IDataFile dataFileArg = (IDataFile) arg;

            if (!dataFileArg.getName().equals(dataFileToBeTested.getName())) {
                return false;
            }

            if (!dataFileArg.getDataset().getId().equals(dataFileToBeTested.getDataset().getId())) {
                return false;
            }

            return dataFileArg.getExtension().equals(dataFileToBeTested.getExtension());
        }
    }
}

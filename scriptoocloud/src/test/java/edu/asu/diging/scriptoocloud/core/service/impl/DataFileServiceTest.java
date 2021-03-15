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

    private static final Long DATASET_ID = 1L;

    private static final String DATASET_ID_STRING = "1";

    private static final Long DATAFILE_ID = 2L;

    private static final int PAGE_SIZE = 25;

    private static final int SECOND_PAGE_SIZE = 10;

    private static final int PAGE_NUMBER = 1;

    private static final String USERNAME = "username";

    private static final String DATASET_NAME = "datasetName";

    private static final String FILE_NAME = "fileName.txt";

    private static final String FILE_EXTENSION = "txt";

    private static final String FILE_TYPE = Dataset.class.getSimpleName().toLowerCase(Locale.ROOT);

    private static final String INDEX_BASED_FILENAME = "2.txt";

    private static final byte[] BYTES = "abc".getBytes();

    private static final OffsetDateTime CREATED_AT = OffsetDateTime.now();

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
        dataFile.setId(DATAFILE_ID);
    }

    private void initDataset() {
        dataset = new Dataset();
        dataset.setUser(user);
        dataset.setName(DATASET_NAME);
        dataset.setId(DATASET_ID);
    }

    private void initFilePage() {
        filePage = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE + SECOND_PAGE_SIZE; i++) {
            DataFile dataFile = new DataFile();
            filePage.add(dataFile);
        }
    }

    @Test
    public void test_createFile() throws FileSystemStorageException {
        dataFile.setDataset((Dataset) dataset);
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.save((DataFile) Mockito.argThat(
                new DataFileArgMatcher(dataFile)))).thenReturn((DataFile) dataFile);
        Assertions.assertDoesNotThrow(() -> dataFileService.createFile(BYTES, DATASET_ID_STRING,
                USERNAME, FILE_NAME, FILE_EXTENSION));
        Mockito.verify(fileSystemService).createFileInDirectory(USERNAME, FILE_TYPE, DATASET_ID_STRING,
                INDEX_BASED_FILENAME, BYTES);
    }

    @Test
    public void test_createFile_failed_data_file_exception() {
        Mockito.when(datasetRepository.findById(DATASET_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataFileStorageException.class, () -> dataFileService.createFile(
                BYTES, DATASET_ID_STRING, USERNAME, FILE_NAME, FILE_EXTENSION));
    }

    @Test
    public void test_createFile_failed_file_system_exception() throws FileSystemStorageException {
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.save((DataFile) Mockito.argThat(
                new DataFileArgMatcher(dataFile)))).thenReturn((DataFile) dataFile);
        dataFile.setDataset((Dataset) dataset);
        Mockito.doThrow(new FileSystemStorageException("Error")).when(fileSystemService)
                .createFileInDirectory(USERNAME, FILE_TYPE, DATASET_ID_STRING,
                        INDEX_BASED_FILENAME, BYTES);
        Assertions.assertThrows(DataFileStorageException.class, () -> dataFileService.createFile(
                BYTES, DATASET_ID_STRING, USERNAME, FILE_NAME, FILE_EXTENSION));
    }

    @Test
    public void test_findFiles() {
        List<DataFile> files = new ArrayList<>();
        files.add((DataFile) dataFile);
        Page<DataFile> fileList = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Mockito.when(dataFileRepository.findAllByDatasetId(DATASET_ID,
                pageRequest)).thenReturn(fileList);
        Assertions.assertEquals(fileList, dataFileService.findFiles(pageRequest, DATASET_ID));
    }

    @Test
    public void test_findFiles_empty() {
        List<DataFile> files = new ArrayList<>();
        Page<DataFile> fileList = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Mockito.when(dataFileRepository.findAllByDatasetId(DATASET_ID,
                pageRequest)).thenReturn(fileList);
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

    static class DataFileArgMatcher extends ArgumentMatcher<IDataFile> {

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

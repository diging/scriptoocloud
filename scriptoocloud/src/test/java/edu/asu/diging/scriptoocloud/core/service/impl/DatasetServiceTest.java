package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.DataFileNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetNotFoundException;
import edu.asu.diging.scriptoocloud.core.exceptions.DatasetStorageException;
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
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.File;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Test class for DataFileService
 *
 * @author John Coronite
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AnnotationConfigContextLoader.class)
public class DatasetServiceTest {

    @InjectMocks
    private DatasetService datasetService;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private DatasetRepository datasetRepository;

    @Mock
    private FileSystemService fileSystemService;

    @TempDir
    public Path path;

    @TempDir
    public File file;

    private final String USERNAME = "username";

    private final String TYPE = Dataset.class.getSimpleName().toLowerCase(Locale.ROOT);

    private final int PAGE_SIZE = 25;

    private static final int SECOND_PAGE_SIZE = 10;

    private final int PAGE_NUMBER = 1;

    private static final Long DATASET_ID = 1L;

    private static final String DATASET_ID_STRING = DATASET_ID.toString();

    private static final String DATASET_NAME = "datasetName";

    private static final String DATAFILE_NAME = "2";

    private static final Long DATA_FILE_ID = 2L;

    private static final String DATA_FILE_EXTENSION = "txt";

    private static final OffsetDateTime CREATED_AT = OffsetDateTime.now();

    private IDataset dataset;

    private IDataFile dataFile;

    private IUser user;

    private List<Dataset> datasetPage;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        initUser();
        initDataset();
        initDatasetPage();
        initDataFile();
    }

    private void initDataset() {
        dataset = new Dataset();
        dataset.setUser(user);
        dataset.setName(DATASET_NAME);
        dataset.setId(DATASET_ID);
    }

    private void initUser() {
        user = new SimpleUser();
        user.setUsername(USERNAME);
    }

    private void initDatasetPage() {
        datasetPage = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE + SECOND_PAGE_SIZE; i++) {
            Dataset dataset = new Dataset();
            datasetPage.add(dataset);
        }
    }

    private void initDataFile() {
        dataFile = new DataFile();
        dataFile.setId(DATA_FILE_ID);
        dataFile.setName(DATAFILE_NAME);
        dataFile.setCreatedAt(CREATED_AT);
        dataFile.setExtension(DATA_FILE_EXTENSION);
    }

    @Test
    public void test_createDataset() throws FileSystemStorageException {
        Mockito.when(datasetRepository.save(((Dataset) Mockito.argThat(
                new DatasetServiceTest.DatasetArgMatcher(dataset))))).thenReturn((Dataset) dataset);
        Assertions.assertDoesNotThrow(() -> datasetService.createDataset(DATASET_NAME, user));
        Mockito.verify(fileSystemService).addDirectories(USERNAME, TYPE, DATASET_ID_STRING);
    }

    @Test
    public void test_createDataset_failed() throws FileSystemStorageException {
        Mockito.when(datasetRepository.save((Dataset) Mockito.argThat(
                new DatasetServiceTest.DatasetArgMatcher(dataset)))).thenReturn((Dataset) dataset);
        Mockito.doThrow(new FileSystemStorageException("Error")).when(fileSystemService)
                .addDirectories(USERNAME, TYPE, DATASET_ID_STRING);
        Assertions.assertThrows(DatasetStorageException.class, () ->
                datasetService.createDataset(DATASET_NAME, user));
    }

    @Test
    public void test_editDataset() {
        Dataset newDataset = new Dataset();
        String newName = "newName";
        newDataset.setName(newName);
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Assertions.assertDoesNotThrow(() -> datasetService.editDataset(dataset.getId(), newName));
        dataset.setName(newName);
        Mockito.when(datasetRepository.save((Dataset) dataset)).thenReturn(newDataset);
        Assertions.assertEquals(newDataset.getName(), dataset.getName());
    }

    @Test
    public void test_editDataset_failed() {
        Dataset newDataset = new Dataset();
        String newName = "newName";
        newDataset.setName(newName);
        Mockito.when(datasetRepository.findById(DATASET_ID)).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(() -> datasetService.editDataset(dataset.getId(), newName));
        // should not be called this time - dataset not present
        Mockito.verify(datasetRepository, Mockito.never()).save((Dataset) dataset);
        Assertions.assertNotEquals(newDataset.getName(), dataset.getName());
    }

    @Test
    public void test_deleteDataset() {
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Assertions.assertDoesNotThrow(() -> datasetService.deleteDataset(DATASET_ID, USERNAME));
        Mockito.verify(dataFileRepository).deleteAllByDatasetId(DATASET_ID);
        Mockito.verify(datasetRepository).deleteById(DATASET_ID);
    }

    @Test
    public void test_deleteDataset_failed() throws FileSystemStorageException {
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.doThrow(new FileSystemStorageException("FileSystemStorageException"))
                .when(fileSystemService)
                .deleteDirectories(USERNAME, TYPE, DATASET_ID_STRING);
        Assertions.assertThrows(DatasetStorageException.class,
                () -> datasetService.deleteDataset(DATASET_ID, USERNAME));
    }

    @Test
    public void test_findById() {
        Mockito.when(datasetRepository.findById(DATASET_ID)).thenReturn(
                Optional.of((Dataset) dataset));
        Assertions.assertEquals(dataset, Optional.of((Dataset) dataset).get());
    }

    @Test
    public void test_findById_null() {
        Mockito.when(datasetRepository.findById(DATASET_ID)).thenReturn(Optional.empty());
        Assertions.assertNull(datasetService.findById(DATASET_ID));
    }

    @Test
    public void test_findDatasets() {
        List<Dataset> datasets = new ArrayList<>();
        datasets.add((Dataset) dataset);
        Page<Dataset> datasetList = new PageImpl<>(datasets);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Mockito.when(datasetRepository.findAllByUser(user, pageRequest)).thenReturn(datasetList);
        Assertions.assertEquals(datasetList, datasetService.findDatasets(pageRequest, user));
    }

    @Test
    public void test_findDatasets_empty() {
        Page<Dataset> datasetList = new PageImpl<>(datasetPage);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Mockito.when(datasetRepository.findAllByUser(user, pageRequest)).thenReturn(datasetList);
        Assertions.assertEquals(datasetList, datasetService.findDatasets(pageRequest, user));
    }

    @Test
    public void test_findDatasets_secondPage() {
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER + 1, PAGE_SIZE);
        Page<Dataset> datasetListSecondPage = new PageImpl<>(datasetPage.subList(PAGE_SIZE - 1,
                (PAGE_SIZE - 1 + SECOND_PAGE_SIZE)));
        Mockito.when(datasetRepository.findAllByUser(user, pageRequest))
                .thenReturn(datasetListSecondPage);
        Assertions.assertEquals(datasetListSecondPage, datasetService.findDatasets(pageRequest, user));
        Assertions.assertEquals(SECOND_PAGE_SIZE, datasetListSecondPage.getTotalElements());
    }

    @Test
    public void test_findAll() {
        List<Dataset> datasets = new ArrayList<>();
        datasets.add((Dataset) dataset);
        Page<Dataset> datasetList = new PageImpl<>(datasets);
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Mockito.when(datasetRepository.findAll(pageRequest)).thenReturn(datasetList);
        Assertions.assertEquals(datasetList, datasetService.findAll(pageRequest));
    }

    @Test
    public void test_deleteFileFromDataset() throws FileSystemStorageException {
        dataset.addFile((DataFile) dataFile);
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.findById(DATA_FILE_ID))
                .thenReturn(Optional.of((DataFile) dataFile));
        Mockito.when(fileSystemService.createPath(USERNAME, TYPE,
                DATASET_ID_STRING)).thenReturn(path);
        Mockito.when(fileSystemService.createPath(USERNAME, TYPE, DATASET_ID_STRING))
                .thenReturn(path);
        Mockito.when(fileSystemService.deleteDirectoryOrFile(file)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> datasetService.deleteFileFromDataset(DATASET_ID,
                DATA_FILE_ID));

        Mockito.verify(datasetRepository).save((Dataset) Mockito
                .argThat(new DatasetServiceTest.DatasetArgMatcher((dataset))));

        Mockito.verify(dataFileRepository).deleteById(DATA_FILE_ID);
        File fileWithName = new File(file + File.separator + DATAFILE_NAME);
        Mockito.verify(fileSystemService).deleteDirectoryOrFile(fileWithName);
    }

    @Test
    public void test_deleteFileFromDataset_no_dataset() {
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DatasetNotFoundException.class,
                () -> datasetService.deleteFileFromDataset(DATASET_ID, DATA_FILE_ID));
    }

    @Test
    public void test_deleteFileFromDataset_no_datafile() {
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.findById(DATA_FILE_ID))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataFileNotFoundException.class,
                () -> datasetService.deleteFileFromDataset(DATASET_ID, DATA_FILE_ID));
    }

    @Test
    public void test_deleteFileFromDataset_no_file_on_server() throws FileSystemStorageException {
        dataset.addFile((DataFile) dataFile);
        Mockito.when(datasetRepository.findById(DATASET_ID))
                .thenReturn(Optional.of((Dataset) dataset));
        Mockito.when(dataFileRepository.findById(DATA_FILE_ID))
                .thenReturn(Optional.of((DataFile) dataFile));
        Mockito.when(fileSystemService.createPath(USERNAME, TYPE, DATASET_ID_STRING))
                .thenReturn(path);
        Mockito.doThrow(new FileSystemStorageException("FileSystemStorageException"))
                .when(fileSystemService).createPath(USERNAME, TYPE, DATASET_ID_STRING);

        Assertions.assertThrows(DatasetStorageException.class,
                () -> datasetService.deleteFileFromDataset(DATASET_ID, DATA_FILE_ID));

        Mockito.verify(datasetRepository).save((Dataset) Mockito
                .argThat(new DatasetServiceTest.DatasetArgMatcher((dataset))));
        Mockito.verify(dataFileRepository).deleteById(DATA_FILE_ID);
    }

    static class DatasetArgMatcher extends ArgumentMatcher<IDataset> {

        private final IDataset datasetToBeTested;

        public DatasetArgMatcher(IDataset dataset) {
            datasetToBeTested = dataset;
        }

        @Override
        public boolean matches(Object arg) {
            IDataset datasetArg = (IDataset) arg;

            if (!datasetArg.getName().equals(datasetToBeTested.getName())) {
                return false;
            }

            return datasetArg.getUsername().equals(datasetToBeTested.getUsername());
        }
    }
}

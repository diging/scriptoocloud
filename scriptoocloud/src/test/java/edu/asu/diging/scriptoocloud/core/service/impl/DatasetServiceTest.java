package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
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

    private final String username = "username";
    private final String type = Dataset.class.getSimpleName().toLowerCase(Locale.ROOT);
    private final int pageSize = 25;
    private final int pageNumber = 1;


    @InjectMocks
    private DatasetService datasetService;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private DatasetRepository datasetRepository;

    @Spy
    private FileSystemService fileSystemService;

    @Spy
    private Dataset dataset;

    @Spy
    private SimpleUser simpleUser;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        simpleUser.setUsername(username);
        dataset.setName(username);
        dataset.setUser(simpleUser);
        dataset.setId(1L);
    }

    @Test
    public void test_createDataset() throws FileSystemStorageException {
        IUser user = simpleUser;
        Mockito.when(datasetRepository.save(Mockito.any())).thenReturn(dataset);
        Mockito.when(dataset.getId()).thenReturn(1L);
        Assertions.assertDoesNotThrow(() -> datasetService.createDataset(username, user));
        Mockito.verify(fileSystemService).addDirectories(username, type, "1");
    }

    @Test
    public void test_editDataset() {
        String newName = "newName";
        Mockito.when(datasetRepository.findById(Mockito.any())).thenReturn(Optional.of(dataset));
        Assertions.assertDoesNotThrow(() -> datasetService.editDataset(dataset.getId(), newName));
        Mockito.verify(datasetRepository).save(dataset);
    }

    @Test
    public void test_deleteDataset() {
        Optional<Dataset> optional = Optional.of(dataset);
        Long datasetId = optional.get().getId();
        Mockito.when(datasetRepository.findById(Mockito.any())).thenReturn(optional);
        Assertions.assertDoesNotThrow(() -> datasetService.deleteDataset(datasetId, username));
        Mockito.verify(dataFileRepository).deleteAllByDatasetId(datasetId);
        Mockito.verify(datasetRepository).deleteById(datasetId);
    }

    @Test
    public void test_findById() {
        Optional<Dataset> optional = Optional.of(dataset);
        Long datasetId = optional.get().getId();
        Mockito.when(datasetRepository.findById(1L)).thenReturn(optional);
        Assertions.assertDoesNotThrow(() -> datasetService.findById(datasetId));
    }

    @Test
    public void test_findById_null() {
        Long datasetId = 1L;
        Mockito.when(datasetRepository.findById(datasetId)).thenReturn(Optional.empty());
        Assertions.assertNull(datasetService.findById(datasetId));
    }

    @Test
    public void test_findDatasets() {
        List<Dataset> datasets = new ArrayList<>();
        Page<Dataset> datasetList = new PageImpl<>(datasets);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        IUser user = simpleUser;
        Mockito.when(datasetRepository.findAllByUser(user, pageRequest)).thenReturn(datasetList);
        Assertions.assertEquals(datasetList, datasetService.findDatasets(pageRequest, user));
    }

    @Test
    public void test_findDatasets_secondPage() {
        List<Dataset> datasets = new ArrayList<>();
        List<Dataset> spyDatasets = Mockito.spy(datasets);
        PageRequest pageRequest = PageRequest.of(pageNumber + 1, pageSize);
        PageRequest spyPageRequest = Mockito.spy(pageRequest);
        int numberInSecondPage = 10;
        for (int i = 0; i < pageSize + numberInSecondPage; i++) {
            Dataset dataset = Mockito.mock(Dataset.class);
            spyDatasets.add(dataset);
        }
        Page<Dataset> datasetListSecondPage = new PageImpl<>(spyDatasets.subList(pageSize - 1,
                (pageSize - 1 + numberInSecondPage)));
        Page<Dataset> spyDatasetList = Mockito.spy(datasetListSecondPage);

        IUser user = simpleUser;
        Mockito.when(datasetRepository.findAllByUser(user, spyPageRequest)).thenReturn(spyDatasetList);
        Assertions.assertEquals(datasetListSecondPage, datasetService.findDatasets(spyPageRequest, user));
        Assertions.assertEquals(numberInSecondPage, spyDatasetList.getTotalElements());
    }

    @Test
    public void test_findAll() {
        List<Dataset> datasets = new ArrayList<>();
        List<Dataset> spyDatasets = Mockito.spy(datasets);
        Page<Dataset> datasetList = new PageImpl<>(spyDatasets);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        PageRequest spyPageRequest = Mockito.spy(pageRequest);
        Mockito.when(datasetRepository.findAll(spyPageRequest)).thenReturn(datasetList);
        Assertions.assertEquals(datasetList, datasetService.findAll(spyPageRequest));
    }

    @Test
    public void test_deleteFileFromDataset() throws FileSystemStorageException {
        Long datasetId = dataset.getId();
        DataFile dataFile = Mockito.spy(DataFile.class);
        Long dataFileId = 2L;
        dataFile.setId(dataFileId);
        String fileName = "fileName";
        dataFile.setName("fileName");
        dataFile.setCreatedAt(OffsetDateTime.now());
        dataFile.setType("txt");
        Set<DataFile> files = new HashSet<>();
        files.add(dataFile);
        File fileToBeDeleted = Mockito.mock(File.class);
        Path path = Mockito.spy(Path.class);
        String pathString = "pathString";

        Mockito.when(dataFileRepository.findById(dataFileId)).thenReturn(Optional.of(dataFile));
        Mockito.when(dataFile.getName()).thenReturn(fileName);
        Mockito.when(datasetRepository.findById(datasetId)).thenReturn(Optional.of(dataset));
        Mockito.when(dataFileRepository.findById(dataFile.getId())).thenReturn(Optional.of(dataFile));
        Mockito.when(dataset.getFiles()).thenReturn(files);
        Mockito.when(fileSystemService.createPath(username, type,
                datasetId.toString())).thenReturn(path);
        Mockito.when(path.toString()).thenReturn(pathString);
        Mockito.when(fileSystemService.deleteDirectoryOrFile(fileToBeDeleted)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> datasetService.deleteFileFromDataset(datasetId,
                dataFileId));

        Mockito.verify(datasetRepository).findById(datasetId);
        Mockito.verify(dataFileRepository).findById(dataFileId);
        Mockito.verify(datasetRepository).save(dataset);
        Mockito.verify(dataFileRepository).deleteById(dataFileId);
    }
}

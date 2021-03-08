package edu.asu.diging.scriptoocloud.core.service.impl;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Test class for DataFileService
 *
 * @author John Coronite
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AnnotationConfigContextLoader.class)
class DataFileServiceTest {

    private final Long datasetId = 1L;
    private final int pageSize = 25;
    private final int pageNumber = 1;

    @InjectMocks
    private DataFileService dataFileService;

    @Mock
    private DataFileRepository dataFileRepository;

    @Mock
    private DatasetRepository datasetRepository;

    @Mock
    private FileSystemService fileSystemService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createFile() throws FileSystemStorageException {

        Dataset dataset = Mockito.mock(Dataset.class);
        Mockito.when(datasetRepository.findById(datasetId))
                .thenReturn(Optional.of(dataset));
        Mockito.when(dataset.addFile(dataFileRepository.save(Mockito.any()))).thenReturn(1L);

        byte[] bytes = "abc".getBytes();
        String datasetIdString = datasetId.toString();
        String username = "username";
        String filename = "filename.txt";
        String type = Dataset.class.getSimpleName().toLowerCase(Locale.ROOT);
        Assertions.assertDoesNotThrow(() -> dataFileService.createFile(bytes, datasetIdString,
                username, filename, type));

        String indexBasedFilename = "1.txt";
        Mockito.verify(datasetRepository).save(dataset);
        Mockito.verify(fileSystemService).createFileInDirectory(username, type, datasetIdString,
                indexBasedFilename, bytes);
    }

    @Test
    public void test_findFiles() {
        List<DataFile> files = new ArrayList<>();
        Page<DataFile> fileList = new PageImpl<>(files);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Mockito.when(dataFileRepository.findAllByDatasetId(datasetId,
                pageRequest)).thenReturn(fileList);
        Assertions.assertEquals(fileList, dataFileService.findFiles(pageRequest, datasetId));
    }

    @Test
    public void test_findFiles_secondPage() {
        int numberInSecondPage = 10;
        List<DataFile> files = new ArrayList<>();
        List<DataFile> spyFiles = Mockito.spy(files);
        PageRequest pageRequest = PageRequest.of(pageNumber + 1, pageSize);
        PageRequest spyPageRequest = Mockito.spy(pageRequest);
        for (int i = 0; i < pageSize + numberInSecondPage; i++) {
            DataFile dataFile = Mockito.mock(DataFile.class);
            spyFiles.add(dataFile);
        }
        Page<DataFile> dataFileListSecondPage = new PageImpl<>(spyFiles.subList(pageSize - 1,
                (pageSize - 1 + numberInSecondPage)));
        Mockito.when(dataFileRepository.findAllByDatasetId(datasetId,
                spyPageRequest)).thenReturn(dataFileListSecondPage);
        Assertions.assertEquals(dataFileListSecondPage, dataFileService.findFiles(spyPageRequest,
                datasetId));
        Assertions.assertEquals(numberInSecondPage, dataFileListSecondPage.getTotalElements());
    }
}

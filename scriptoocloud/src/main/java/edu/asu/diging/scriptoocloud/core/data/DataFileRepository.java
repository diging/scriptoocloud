package edu.asu.diging.scriptoocloud.core.data;

import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataFileRepository extends PagingAndSortingRepository<DataFile, Long> {

    List<DataFile> findAllByDataset_Id(Long id);

    void deleteAllById(Long datasetId);
}

package edu.asu.diging.scriptoocloud.core.data;

import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataFileRepository extends PagingAndSortingRepository<DataFile, Long> {

    Page<DataFile> findAllByDatasetId(Long id, Pageable pageable);

    void deleteAllByDatasetId(Long datasetId);
}
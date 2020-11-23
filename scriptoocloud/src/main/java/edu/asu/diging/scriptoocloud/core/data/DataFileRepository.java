package edu.asu.diging.scriptoocloud.core.data;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataFileRepository extends PagingAndSortingRepository<DataFile, Long> {

    Optional<DataFile> findByName(String name);

    Page<DataFile> findAllByDataset_Id(Long id, Pageable pageable);
}

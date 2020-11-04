package edu.asu.diging.scriptoocloud.core.data;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface DataFileRepository extends PagingAndSortingRepository<DataFile, Long> {

    Optional<DataFile> findByName(String name);

    Set<DataFile> findAllByDatasetId(Long id );
}

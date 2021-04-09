package edu.asu.diging.scriptoocloud.core.data;

import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasetRepository extends PagingAndSortingRepository<Dataset, Long> {

    Page<Dataset> findAllByUser(IUser user, Pageable pageable);
}

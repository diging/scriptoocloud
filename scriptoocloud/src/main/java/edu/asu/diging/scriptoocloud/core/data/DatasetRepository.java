package edu.asu.diging.scriptoocloud.core.data;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DatasetRepository extends PagingAndSortingRepository<Dataset, Long> {

    Dataset findByName(String name);

    Optional<Dataset> findById(Long id);

    Page<Dataset> findAllByUser(IUser user, Pageable pageable);

    Page<Dataset> findAll(Pageable pageable);
}

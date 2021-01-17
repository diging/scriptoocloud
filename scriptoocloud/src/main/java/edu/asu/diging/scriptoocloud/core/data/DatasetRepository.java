package edu.asu.diging.scriptoocloud.core.data;

import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DatasetRepository extends PagingAndSortingRepository<Dataset, Long> {

    Optional<Dataset> findById(Long id);

    List<Dataset> findAllByUser(IUser user);

    List<Dataset> findAll();
}

package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.model.impl.YamlModel;

public interface YamlModelRepository extends PagingAndSortingRepository<YamlModel, Long> {

    public YamlModel findByName(String name);
    
    public YamlModel deleteByName(String name);
}

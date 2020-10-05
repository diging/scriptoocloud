package edu.asu.diging.scriptoocloud.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.asu.diging.scriptoocloud.core.model.Repository;

public interface RepoRepository extends JpaRepository<Repository, Long> {

}

package edu.asu.diging.scriptoocloud.core.service;

import java.util.ArrayList;

import edu.asu.diging.scriptoocloud.core.model.GitRepository;

public interface ListGitRepositoriesService {

    ArrayList<GitRepository> getRepos();

}
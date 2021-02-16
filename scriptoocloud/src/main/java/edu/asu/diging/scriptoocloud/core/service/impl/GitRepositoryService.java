package edu.asu.diging.scriptoocloud.core.service.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import edu.asu.diging.scriptoocloud.core.exceptions.FileSystemStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.scriptoocloud.core.data.GitRepositoryRepository;
import edu.asu.diging.scriptoocloud.core.exceptions.InvalidGitUrlException;
import edu.asu.diging.scriptoocloud.core.model.GitRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.GitRepositoryImpl;
import edu.asu.diging.scriptoocloud.core.service.GitRepositoryManager;
import edu.asu.diging.scriptoocloud.core.service.JgitService;
import edu.asu.diging.scriptoocloud.core.service.UrlFormatterUtility;

/**
 * The Service class which clones remote git repositories and facilitates transactional events
 * related to git repositories in file system
 *
 * @author Jason Ormsby
 */

@Service
@Transactional
@PropertySource("classpath:config.properties")
public class GitRepositoryService implements GitRepositoryManager {

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private UrlFormatterUtility urlFormatter;

    @Autowired
    private GitRepositoryRepository gitRepositoryJpa;

    @Autowired
    private JgitService jGitService;

    @Value("${git.repositories.path}")
    public String path;

    /**
     * Checks system path property has trailing slash to facilitate concatenation with new file names
     */
    @PostConstruct
    private void validatePathProperty() {
        if (!path.endsWith("/")) {
            path += "/";
        }
    }

    @Override
    public void cloneRepository(String gitUrl, String requester) throws InvalidGitUrlException,
            MalformedURLException, FileSystemStorageException {
        String folderName = urlFormatter.urlToFolderName(gitUrl);

        ZonedDateTime creationDate = ZonedDateTime.now();

        GitRepositoryImpl repositoryEntity = gitRepositoryJpa.findByUrl(gitUrl);
        if (repositoryEntity == null) {
            repositoryEntity = new GitRepositoryImpl();
        } else {
            fileSystemService.deleteDirectoryOrFile(new File(path + repositoryEntity.getFolderName()));
        }

        repositoryEntity.setUrl(gitUrl);
        repositoryEntity.setRequester(requester);
        repositoryEntity.setCreationDate(creationDate);
        repositoryEntity.setFolderName(folderName);

        jGitService.clone(path + folderName, gitUrl);
        gitRepositoryJpa.save(repositoryEntity);
    }

    @Override
    public ArrayList<GitRepository> listRepositories() {
        Iterable<GitRepositoryImpl> repoModels = gitRepositoryJpa.findAll();
        ArrayList<GitRepository> reposList = new ArrayList<>();
        repoModels.iterator().forEachRemaining(r -> reposList.add(r));
        return reposList;
    }

    @Override
    public void deleteRepository(Long id) throws FileSystemStorageException {
        GitRepositoryImpl gitRepository = gitRepositoryJpa.findById(id).get();
        gitRepositoryJpa.deleteById(gitRepository.getId());
        File file = new File(path + gitRepository.getFolderName());
        fileSystemService.deleteDirectoryOrFile(file);
    }

}

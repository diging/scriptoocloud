package edu.asu.diging.scriptoocloud.config;

import edu.asu.diging.scriptoocloud.core.data.DataFileRepository;
import edu.asu.diging.scriptoocloud.core.data.DatasetRepository;
import edu.asu.diging.scriptoocloud.core.model.impl.DataFile;
import edu.asu.diging.scriptoocloud.core.model.impl.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private DatasetRepository datasetRepository;
    @Autowired
    private DataFileRepository dataFileRepository;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        // Implement functionality If needed for other domain objects
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        if (targetType.equals(Dataset.class.getSimpleName())) {
            Optional<Dataset> dataset = datasetRepository.findById((Long) targetId);
            if (dataset.isPresent()) {
                // return true if Dataset owner is making the request
                return dataset.get().getUsername().equals(auth.getName());
            }
        }
        if (targetType.equals(DataFile.class.getSimpleName())) {
            Optional<DataFile> dataFile = dataFileRepository.findById((Long) targetId);
            if (dataFile.isPresent()) {
                // return true if DataFile owner is making the request
                return dataFile.get().getOwner().equals(auth.getName());
            }
        }
        return false;
    }

}

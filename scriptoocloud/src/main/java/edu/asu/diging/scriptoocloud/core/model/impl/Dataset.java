package edu.asu.diging.scriptoocloud.core.model.impl;

import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class for user-supplied Datasets. Implements {@code IDataset}.
 * {@code SimpleUser} has many {@code Dataset}s and a {@code Dataset} has many {@code DataFiles}.
 *
 * @author John Coronite
 */
@Entity
public class Dataset implements IDataset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private SimpleUser user;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "dataset", orphanRemoval = true)
    private Set<DataFile> files;

    /**
     * Set the {@code Dataset} id.
     *
     * @param id The {@code Dataset} id.
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the {@code Dataset} id.
     *
     * @return The {@code Dataset} id.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Set the {@code Dataset} name.
     *
     * @param name The {@code Dataset} name.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the {@code Dataset} name.
     *
     * @return The {@code Dataset} name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the {@code Dataset}'s associated {@code SimpleUser}.
     *
     * @param user The {@code SimpleUser} associated with the Dataset.
     */
    @Override
    public void setUser(IUser user) {
        this.user = (SimpleUser) user;
    }

    /**
     * Get the {@code Dataset}'s associated {@code SimpleUser} username.
     *
     * @return The username of the {@code SimpleUser} associated with the {@code Dataset}.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Get the {@code Set} of {@code DataFile}s belonging to the {@code Dataset}.
     *
     * @return The {@code Set} of {@code DataFile}s belonging to the {@code Dataset}.
     */
    @Override
    public Set<DataFile> getFiles() {
        return this.files;
    }

    /**
     * Add a {@code DataFile} to the {@code Dataset}.
     *
     * @param dataFile The {@code DataFile} to add.
     */
    @Override
    public Long addFile(DataFile dataFile) {
        if (files == null){
            files = new HashSet<>();
        }
        if (dataFile == null){
            dataFile = new DataFile();
        }
        files.add(dataFile);
        return dataFile.getId();
    }
}

package edu.asu.diging.scriptoocloud.core.model.impl;

import edu.asu.diging.scriptoocloud.core.model.IDataFile;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entity class for user-supplied DataFiles. Implements {@code IDataFile}
 * Each {code @Dataset} has many {@code DataFile}s.
 *
 * @author John Coronite
 */
@Entity
public class DataFile implements IDataFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String extension;
    @ManyToOne
    @JoinColumn(name = "dataset_id", nullable = false)
    private Dataset dataset;
    private OffsetDateTime createdAt;

    /**
     * Set the {@code DataFile} id.
     *
     * @param id The {@code DataFile} id.
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the {@code DataFile} id.
     *
     * @return The {@code DataFile} id.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Set the {@code DataFile} name.
     *
     * @param name The {@code DataFile} name.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the {@code DataFile} name.
     *
     * @return The {@code DataFile} name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the {@code DataFile} file extension.
     *
     * @param type The {@code DataFile} type.
     */
    public void setExtension(String type) {
        this.extension = type;
    }

    /**
     * Get the {@code DataFile} file extension.
     *
     * @return The {@code DataFile} type.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Set the {@code DataSet} to which the {@code DataFile} belongs.
     *
     * @param dataset The {@code DataSet} to which the {@code DataFile} belongs.
     */
    @Override
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Get the {@code DataSet} to which the {@code DataFile} belongs.
     *
     * @return The {@code DataSet} to which the {@code DataFile} belongs.
     */
    @Override
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * Set the {@code OffsetDateTime} at which the {@code DataFile} was created.
     *
     * @param createdAt The {@code OffsetDateTime} at which the {@code DataFile} was created.
     */
    @Override
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the {@code OffsetDateTime} at which the {@code DataFile} was created.
     *
     * @return The {@code OffsetDateTime} at which the {@code DataFile} was created.
     */
    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the username to which the {code @Dataset} containing the {@code DataFile} belongs.
     *
     * @return The username.
     */
    @Override
    public String getOwner() {
        return this.dataset.getUsername();
    }
}

package edu.asu.diging.scriptoocloud.core.model.impl;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class DataFile {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String type;
    @ManyToOne
    @JoinColumn(name="datasetId", nullable=false)
    private Dataset dataset;
    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

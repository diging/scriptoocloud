package edu.asu.diging.scriptoocloud.core.model.impl;

import edu.asu.diging.scriptoocloud.core.model.IDataset;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Dataset implements IDataset {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name="username", nullable=false)
    private SimpleUser user;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy="dataset", orphanRemoval = true)
    private Set<DataFile> files;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id){
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setUser(IUser user) {
        this.user = (SimpleUser)user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Set<DataFile> getFiles() {
        return this.files;
    }

    public void addFile(DataFile dataFile) {
        files.add(dataFile);
    }

    public void removeFile(DataFile dataFile){
        files.remove(dataFile);
    }

}

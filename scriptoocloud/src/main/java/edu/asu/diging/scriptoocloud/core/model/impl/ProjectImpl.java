package edu.asu.diging.scriptoocloud.core.model.impl;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import edu.asu.diging.scriptoocloud.core.model.Project;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

@Entity
public class ProjectImpl implements Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;
    private String description;
    private ZonedDateTime creationDate;
    private ZonedDateTime lastModifiedDate;

    private SimpleUser user;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.scriptoocloud.core.model.impl.Project#getId()
     */
    @Override
    public long getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.scriptoocloud.core.model.impl.Project#setId(long)
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.scriptoocloud.core.model.impl.Project#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.scriptoocloud.core.model.impl.Project#setName(java.lang.
     * String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.scriptoocloud.core.model.impl.Project#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.scriptoocloud.core.model.impl.Project#setDescription(java.lang
     * .String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.scriptoocloud.core.model.impl.Project#getCreationDate()
     */
    @Override
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.scriptoocloud.core.model.impl.Project#setCreationDate(java.
     * time.ZonedDateTime)
     */
    @Override
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public void setUser(IUser user) {
        this.user = (SimpleUser) user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}

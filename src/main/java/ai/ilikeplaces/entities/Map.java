package ai.ilikeplaces.entities;

import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 1, 2010
 * Time: 10:46:46 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCycleListener.class)
public class Map {

    private Integer id;

    private String label;

    private String entry;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    @Column(length = 255)
    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    @Column(length = 1000)
    public String getEntry() {
        return entry;
    }

    public void setEntry(final String entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", entry='" + entry + '\'' +
                '}';
    } 
}

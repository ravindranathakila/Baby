package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.etc.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 10:46:46 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "Map", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Map implements Serializable {
// ------------------------------ FIELDS ------------------------------

    @Id
    @Column(name = "id")
    @GeneratedValue
    public Integer id;

    @Column(name = "label", length = 255)
    public String label;

    @Column(name = "entry", length = 1000)
    public String entry;

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getEntry() {
        return entry;
    }

    public void setEntry(final String entry) {
        this.entry = entry;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", entry='" + entry + '\'' +
                '}';
    }
}

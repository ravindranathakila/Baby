package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 1, 2010
 * Time: 10:46:46 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Map implements Serializable {

    public Integer id;

    public String label;

    public String entry;

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

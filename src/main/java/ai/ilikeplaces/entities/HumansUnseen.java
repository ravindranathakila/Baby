package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.doc._unidirectional;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/12/11
 * Time: 7:47 PM
 */
@WARNING("THIS ENTITY IS NOT GUARANTEED TO 'BE' EVEN THOUGH A HUMAN IS SIGNED UP. SO CREATE IT IF NOT PRESENT!")
@Table(name = "HumansUnseen", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansUnseen implements Serializable {
// ------------------------------ FIELDS ------------------------------

    @Id
    public String humanId;


    @_unidirectional
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "humanId")
    public List<Wall> unseenWalls;

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId) {
        this.humanId = humanId;
    }

    public List<Wall> getUnseenWalls() {
        return unseenWalls;
    }

    public void setUnseenWalls(final List<Wall> unseenWalls) {
        this.unseenWalls = unseenWalls;
    }

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public HumansUnseen setHumanIdR(final String humanId) {
        this.humanId = humanId;
        return this;
    }
}

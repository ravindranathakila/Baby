package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.UNIDIRECTIONAL;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/12/11
 * Time: 7:47 PM
 */
@WARNING("THIS ENTITY IS NOT GUARANTEED TO 'BE' EVEN THOUGH A HUMAN IS SIGNED UP. SO CREATE IT IF NOT PRESENT!")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansUnseen implements Serializable {
    public String humanId;
    public List<Wall> unseenWalls;

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId) {
        this.humanId = humanId;
    }

    @Transient
    public HumansUnseen setHumanIdR(final String humanId) {
        this.humanId = humanId;
        return this;
    }

    @UNIDIRECTIONAL
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    public List<Wall> getUnseenWalls() {
        return unseenWalls;
    }

    public void setUnseenWalls(final List<Wall> unseenWalls) {
        this.unseenWalls = unseenWalls;
    }
}

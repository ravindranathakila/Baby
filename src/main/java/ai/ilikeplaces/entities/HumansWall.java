package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc._bidirectional;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Moved to a separate entity to make loading fast and granular.
 * Walls will be used a lot.
 * Also they tend to be bulky.
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "HumansWall", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansWall implements HumanPkJoinFace, Serializable {
// ------------------------------ FIELDS ------------------------------

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "humanId")
    public String humanId;


    @OneToOne(mappedBy = "humanId", cascade = CascadeType.REFRESH)
    //@PrimaryKeyJoinColumn
    public Human human;

    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL)
    public Wall wall;//Convention would be naming this humansWallWall :-(

// --------------------- GETTER / SETTER METHODS ---------------------

    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(final Wall wall) {
        this.wall = wall;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "HumansWall{" +
                "humanId='" + humanId + '\'' +
                '}';
    }
}

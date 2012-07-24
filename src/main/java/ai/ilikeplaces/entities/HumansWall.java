package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
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
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansWall implements HumanPkJoinFace, Serializable {

    private static final long serialVersionUID = 1L;
    public String humanId;
    public Human human;
    public Wall wall;//Convention would be naming this humansWallWall :-(

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @OneToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @OneToOne(cascade = CascadeType.ALL)
    public Wall getWall() {
        return wall;
    }

    public void setWall(final Wall wall) {
        this.wall = wall;
    }

    @Override
    public String toString() {
        return "HumansWall{" +
                "humanId='" + humanId + '\'' +
                '}';
    }
}
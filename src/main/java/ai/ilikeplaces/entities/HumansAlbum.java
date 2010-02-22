package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;

/**
 * This entity is NOT related to a humans public location bookings.
 * This comment was placed here to avoid logic confusion.
 * <p/>
 * User: Ravindranath Akila
 * Date: Dec 6, 2009
 * Time: 6:11:09 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCycleListener.class)
public class HumansAlbum implements HumanPkJoinFace {

    private String humanId;

    private Human human;


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
}
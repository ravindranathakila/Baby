package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.CASCADE;
import ai.ilikeplaces.util.EntityLifeCyleListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 10:26:37 PM
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class HumansPrivateEvent implements HumanPkJoinFace {

    private String humanId;

    private Human human;

    private List<PrivateEvent> privateEvents;


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

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEvents() {
        return privateEvents;
    }

    public void setPrivateEvents(final List<PrivateEvent> privateEvents) {
        this.privateEvents = privateEvents;
    }
}

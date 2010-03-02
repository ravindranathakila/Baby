package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 12, 2010
 * Time: 10:26:37 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCycleListener.class)
public class HumansPrivateEvent implements HumanPkJoinFace {

    private String humanId;

    private Human human;

    private List<PrivateEvent> privateEventsOwned;
    private List<PrivateEvent> privateEventsViewed;
    private List<PrivateEvent> privateEventsInvited;
    private List<PrivateEvent> privateEventsRejected;


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
    @ManyToMany(mappedBy = PrivateEvent.privateEventOwnersCOL, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEventsOwned() {
        return privateEventsOwned;
    }

    public void setPrivateEventsOwned(List<PrivateEvent> privateEventsOwned) {
        this.privateEventsOwned = privateEventsOwned;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventViewersCOL, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEventsViewed() {
        return privateEventsViewed;
    }

    public void setPrivateEventsViewed(List<PrivateEvent> privateEventsViewed) {
        this.privateEventsViewed = privateEventsViewed;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventInvitesCOL, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEventsInvited() {
        return privateEventsInvited;
    }

    public void setPrivateEventsInvited(List<PrivateEvent> privateEventsInvited) {
        this.privateEventsInvited = privateEventsInvited;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventRejectsCOL, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEventsRejected() {
        return privateEventsRejected;
    }

    public void setPrivateEventsRejected(List<PrivateEvent> privateEventsRejected) {
        this.privateEventsRejected = privateEventsRejected;
    }
}

package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.util.EntityLifeCyleListener;

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
@EntityListeners(EntityLifeCyleListener.class)
public class HumansPrivateEvent implements HumanPkJoinFace {

    private String humanId;

    private Human human;

    private List<PrivateEvent> privateEventOwned;
    private List<PrivateEvent> privateEventViewed;
    private List<PrivateEvent> privateEventInvited;
    private List<PrivateEvent> privateEventRejected;


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
    public List<PrivateEvent> getPrivateEventOwned() {
        return privateEventOwned;
    }

    public void setPrivateEventOwned(List<PrivateEvent> privateEventOwned) {
        this.privateEventOwned = privateEventOwned;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventViewersCOL, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEventViewed() {
        return privateEventViewed;
    }

    public void setPrivateEventViewed(List<PrivateEvent> privateEventViewed) {
        this.privateEventViewed = privateEventViewed;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventInvitesCOL, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEventInvited() {
        return privateEventInvited;
    }

    public void setPrivateEventInvited(List<PrivateEvent> privateEventInvited) {
        this.privateEventInvited = privateEventInvited;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventRejectsCOL, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateEvent> getPrivateEventRejected() {
        return privateEventRejected;
    }

    public void setPrivateEventRejected(List<PrivateEvent> privateEventRejected) {
        this.privateEventRejected = privateEventRejected;
    }
}

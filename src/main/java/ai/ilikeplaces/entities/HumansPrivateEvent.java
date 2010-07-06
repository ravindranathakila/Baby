package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.Return;

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
public class HumansPrivateEvent extends HumanEquals implements HumanPkJoinFace,HumansFriend {

    public String humanId;

    public Human human;

    public List<PrivateEvent> privateEventsOwned;
    public List<PrivateEvent> privateEventsViewed;
    public List<PrivateEvent> privateEventsInvited;
    public List<PrivateEvent> privateEventsRejected;


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

    @NOTE(note = "This implementation will be fast a.l.a the Human entity has lazy in its getters.")
    @Override
    @Transient
    public String getDisplayName() {
        return this.human.getHumansNet().getDisplayName();
    }

    @Override
    @Transient
    public boolean isFriend(final String friendsHumanId) {
        final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doDirtyIsHumansNetPeople(new ai.ilikeplaces.logic.validators.unit.HumanId(this.humanId), new ai.ilikeplaces.logic.validators.unit.HumanId(friendsHumanId));
        if (r.returnStatus() != 0) {
            throw new DBException(r.returnError());
        }
        return r.returnValue();
    }

    @Override
    public boolean notFriend(final String friendsHumanId) {
        return !isFriend(friendsHumanId);        
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventOwnersCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> getPrivateEventsOwned() {
        return privateEventsOwned;
    }

    public void setPrivateEventsOwned(List<PrivateEvent> privateEventsOwned) {
        this.privateEventsOwned = privateEventsOwned;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventViewersCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> getPrivateEventsViewed() {
        return privateEventsViewed;
    }

    public void setPrivateEventsViewed(List<PrivateEvent> privateEventsViewed) {
        this.privateEventsViewed = privateEventsViewed;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventInvitesCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> getPrivateEventsInvited() {
        return privateEventsInvited;
    }

    public void setPrivateEventsInvited(List<PrivateEvent> privateEventsInvited) {
        this.privateEventsInvited = privateEventsInvited;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventRejectsCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> getPrivateEventsRejected() {
        return privateEventsRejected;
    }

    public void setPrivateEventsRejected(List<PrivateEvent> privateEventsRejected) {
        this.privateEventsRejected = privateEventsRejected;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (getClass() == o.getClass()) {
            final HumansPrivateEvent that = (HumansPrivateEvent) o;
            return (!(this.getHumanId() == null || that.getHumanId() == null)) && this.getHumanId().equals(that.getHumanId());
        } else {
            return matchHumanId(o);
        }
    }
}

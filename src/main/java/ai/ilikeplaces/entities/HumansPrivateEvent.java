package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc._bidirectional;
import ai.ilikeplaces.entities.etc.*;
import ai.ilikeplaces.logic.mail.GetMailAddress;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 12, 2010
 * Time: 10:26:37 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "HumansPrivateEvent", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansPrivateEvent extends HumanEquals implements HumanPkJoinFace, HumansFriend, GetMailAddress, HumanIdFace, Serializable {
// ------------------------------ FIELDS ------------------------------

    @Id
    @Column(name = "humanId")
    public String humanId;
    public static final String humanIdCOL = "humanId";


    @OneToOne(mappedBy = Human.humansPrivateEventCOL, cascade = CascadeType.REFRESH)
    //@PrimaryKeyJoinColumn
    public Human human;

    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventOwnersCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> privateEventsOwned;
    public static final String privateEventsOwnedCOL = "privateEventsOwned";

    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventViewersCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> privateEventsViewed;
    public static final String privateEventsViewedCOL = "privateEventsViewed";

    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventInvitesCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> privateEventsInvited;
    public static final String privateEventsInvitedCOL = "privateEventsInvited";

    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @ManyToMany(mappedBy = PrivateEvent.privateEventRejectsCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivateEvent> privateEventsRejected;
    public static final String privateEventsRejectedCOL = "privateEventsRejected";

// --------------------- GETTER / SETTER METHODS ---------------------

// ------------------------ ACCESSORS / MUTATORS ------------------------

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

    public List<PrivateEvent> getPrivateEventsInvited() {
        return privateEventsInvited;
    }

    public void setPrivateEventsInvited(List<PrivateEvent> privateEventsInvited) {
        this.privateEventsInvited = privateEventsInvited;
    }

    public List<PrivateEvent> getPrivateEventsOwned() {
        return privateEventsOwned;
    }

    public void setPrivateEventsOwned(List<PrivateEvent> privateEventsOwned) {
        this.privateEventsOwned = privateEventsOwned;
    }

    public List<PrivateEvent> getPrivateEventsRejected() {
        return privateEventsRejected;
    }

    public void setPrivateEventsRejected(List<PrivateEvent> privateEventsRejected) {
        this.privateEventsRejected = privateEventsRejected;
    }

    public List<PrivateEvent> getPrivateEventsViewed() {
        return privateEventsViewed;
    }

    public void setPrivateEventsViewed(List<PrivateEvent> privateEventsViewed) {
        this.privateEventsViewed = privateEventsViewed;
    }

// ------------------------ CANONICAL METHODS ------------------------

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

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface GetMailAddress ---------------------

    /**
     * @return Email Address
     */
    @Override
    public String email() {
        return this.getHumanId();
    }

// --------------------- Interface HumansFriend ---------------------

    @NOTE(note = "This implementation will be fast a.l.a the Human entity has lazy in its getters.")
    @Override
    @Transient
    public String getDisplayName() {
        return this.human.getHumansNet().getDisplayName();
    }

    @Override
    @Transient
    public boolean ifFriend(final String friendsHumanId) {
        return FriendUtil.check(new ai.ilikeplaces.logic.validators.unit.HumanId(this.humanId), new ai.ilikeplaces.logic.validators.unit.HumanId(friendsHumanId)).returnValueBadly();
    }

    @Override
    public boolean notFriend(final String friendsHumanId) {
        return !ifFriend(friendsHumanId);
    }
}

package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 12, 2010
 * Time: 8:19:40 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@CREATED_BY(who = {PrivateEvent.class},
            note = "We need to return this entity to the user for CRUD, hence cascade creation not possible.")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class PrivateEvent implements RefreshData<PrivateEvent>, Serializable {
// ------------------------------ FIELDS ------------------------------

    final static public String privateEventOwnersCOL = "privateEventOwners";
    final static public String privateEventViewersCOL = "privateEventViewers";
    final static public String privateEventInvitesCOL = "privateEventInvites";
    final static public String privateEventRejectsCOL = "privateEventRejects";
    final static public String privateLocationCOL = "privateLocation";
    final static public String locationCOL = "location";
    final static public String privateEventAlbumCOL = "privateEventAlbum";

    public Long privateEventId;

    public String privateEventName;

    public String privateEventInfo;

    public String privateEventStartDate;

    public String privateEventEndDate;

    public Boolean extendedAccess;

    public Wall privateEventWall;

    public List<HumansPrivateEvent> privateEventOwners;

    public List<HumansPrivateEvent> privateEventViewers;

    public List<HumansPrivateEvent> privateEventInvites;

    public List<HumansPrivateEvent> privateEventRejects;

    public PrivateLocation privateLocation;

    public Location location;

    public Album privateEventAlbum;

// ------------------------ ACCESSORS / MUTATORS ------------------------

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @UNIDIRECTIONAL
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Album getPrivateEventAlbum() {
        return privateEventAlbum;
    }

    public void setPrivateEventAlbum(Album privateEventAlbum) {
        this.privateEventAlbum = privateEventAlbum;
    }

    public String getPrivateEventEndDate() {
        return privateEventEndDate;
    }

    public void setPrivateEventEndDate(String privateEventEndDate) {
        this.privateEventEndDate = privateEventEndDate;
    }

    @Id
    public Long getPrivateEventId() {
        return privateEventId;
    }

    public void setPrivateEventId(final Long privateEventId) {
        this.privateEventId = privateEventId;
    }

    public String getPrivateEventInfo() {
        return privateEventInfo;
    }

    public void setPrivateEventInfo(String privateEventInfo) {
        this.privateEventInfo = privateEventInfo;
    }

    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<HumansPrivateEvent> getPrivateEventInvites() {
        return privateEventInvites;
    }

    public void setPrivateEventInvites(final List<HumansPrivateEvent> privateEventInvites) {
        this.privateEventInvites = privateEventInvites;
    }

    public String getPrivateEventName() {
        return privateEventName;
    }

    public void setPrivateEventName(String privateEventName) {
        this.privateEventName = privateEventName;
    }

    @NOTE("Viewers of a private event have no implication except that they could be used as 'people who can see shared items' in future. " +
            "So far, there is no use of this. We are hiding it for this release 2011/09/19 in the user interface")
    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<HumansPrivateEvent> getPrivateEventOwners() {
        return privateEventOwners;
    }

    public void setPrivateEventOwners(final List<HumansPrivateEvent> privateEventOwners) {
        this.privateEventOwners = privateEventOwners;
    }

    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<HumansPrivateEvent> getPrivateEventRejects() {
        return privateEventRejects;
    }

    public void setPrivateEventRejects(final List<HumansPrivateEvent> privateEventRejects) {
        this.privateEventRejects = privateEventRejects;
    }

    public String getPrivateEventStartDate() {
        return privateEventStartDate;
    }

    public void setPrivateEventStartDate(String privateEventStartDate) {
        this.privateEventStartDate = privateEventStartDate;
    }

    @WARNING(warning = "Owner because once an event needs to be deleted, deleting this entity is easier if owner." +
            "If this entity is not the owner, individual owner viewer accepteee rejectee will have to delete their events individually.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<HumansPrivateEvent> getPrivateEventViewers() {
        return privateEventViewers;
    }

    public void setPrivateEventViewers(final List<HumansPrivateEvent> privateEventViewers) {
        this.privateEventViewers = privateEventViewers;
    }

    @UNIDIRECTIONAL
    @OneToOne(cascade = CascadeType.ALL)
    public Wall getPrivateEventWall() {
        return privateEventWall;
    }

    public void setPrivateEventWall(final Wall privateEventWall) {
        this.privateEventWall = privateEventWall;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public PrivateLocation getPrivateLocation() {
        return privateLocation;
    }

    public void setPrivateLocation(PrivateLocation privateLocation) {
        this.privateLocation = privateLocation;
    }

    public Boolean isExtendedAccess() {
        return extendedAccess;
    }

    public void setExtendedAccess(Boolean extendedAccess) {
        this.extendedAccess = extendedAccess;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface RefreshData ---------------------

    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @throws ai.ilikeplaces.exception.DBFetchDataException
     *
     */
    @Override
    public PrivateEvent refresh() throws DBFetchDataException {
        try {
            this.getPrivateEventInvites().size();
            this.getPrivateEventOwners().size();
            this.getPrivateEventViewers().size();
            this.getPrivateEventRejects().size();
        } catch (final Exception e__) {
            throw new DBFetchDataException(e__);
        }
        return this;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "PrivateEvent{" +
                ", privateEventId=" + privateEventId +
                ", privateLocation=" + privateLocation +
                '}';
    }

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public PrivateEvent setExtendedAccessR(Boolean extendedAccess) {
        setExtendedAccess(extendedAccess);
        return this;
    }

    @Transient
    public PrivateEvent setLocationR(Location location) {
        setLocation(location);
        return this;
    }

    @Transient
    public PrivateEvent setPrivateEventAlbumR(Album privateEventAlbum) {
        this.privateEventAlbum = privateEventAlbum;
        return this;
    }

    @Transient
    public PrivateEvent setPrivateEventEndDateR(String privateEventEndDate) {
        setPrivateEventEndDate(privateEventEndDate);
        return this;
    }

    @Transient
    public PrivateEvent setPrivateEventIdR(final Long privateEventId) {
        setPrivateEventId(privateEventId);
        return this;
    }

    @Transient
    public PrivateEvent setPrivateEventInfoR(String privateEventInfo) {
        setPrivateEventInfo(privateEventInfo);
        return this;
    }

    @Transient
    public PrivateEvent setPrivateEventNameR(String privateEventName) {
        setPrivateEventName(privateEventName);
        return this;
    }

    @Transient
    public PrivateEvent setPrivateEventStartDateR(String privateEventStartDate) {
        setPrivateEventStartDate(privateEventStartDate);
        return this;
    }

    @Transient
    public PrivateEvent setPrivateEventWallR(final Wall privateEventWall) {
        this.privateEventWall = privateEventWall;
        return this;
    }

    @Transient
    public PrivateEvent setPrivateLocationR(PrivateLocation privateLocation) {
        this.privateLocation = privateLocation;
        return this;
    }
}

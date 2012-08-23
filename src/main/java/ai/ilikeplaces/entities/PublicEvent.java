package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.exception.DBFetchDataException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Feb 09, 2022
 * Time: 8:00:40 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@CREATED_BY(who = {PublicEvent.class},
        note = "We need to return this entity to the user for CRUD, hence cascade creation not possible.")

@DOCUMENTATION(
        SEE = @SEE({PublicEvent.class, Location.class}),
        LOGIC = @LOGIC(
                {
                        @NOTE("Used with PrivateEvent."),
                        @NOTE("Has an expiry mechanism (outdated events)"),
                        @NOTE("Always assigned to the head of a PrivateEvent tree"),
                        @NOTE("A PrivateEvent tree is the set of PrivateEvent's saying they are Public"),
                        @NOTE("Each PrivateEvent enlisted under this Public event, belongs to one tree"),
                        @NOTE("PublicEvent should also be allocation a Location"),
                        @NOTE("Location allocation will help enlist on location pages")
                }
        )
)
//@Entity
public class PublicEvent implements RefreshData<PublicEvent>, Serializable {
// ------------------------------ FIELDS ------------------------------

    final static public String privateLocationCOL = "privateLocation";
    final static public String locationCOL = "location";
    final static public String publicEventAlbumCOL = "publicEventAlbum";

    public Long publicEventId;

    public String publicEventName;

    public String publicEventInfo;

    public String publicEventStartDate;

    public String publicEventEndDate;

    public Boolean extendedAccess;

    public Wall publicEventWall;

    public PrivateLocation privateLocation;

    public Location location;

// ------------------------ ACCESSORS / MUTATORS ------------------------

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getPublicEventEndDate() {
        return publicEventEndDate;
    }

    public void setPublicEventEndDate(String publicEventEndDate) {
        this.publicEventEndDate = publicEventEndDate;
    }

    @Id
    public Long getPublicEventId() {
        return publicEventId;
    }

    public void setPublicEventId(final Long publicEventId) {
        this.publicEventId = publicEventId;
    }

    public String getPublicEventInfo() {
        return publicEventInfo;
    }

    public void setPublicEventInfo(String publicEventInfo) {
        this.publicEventInfo = publicEventInfo;
    }


    public String getPublicEventName() {
        return publicEventName;
    }

    public void setPublicEventName(String publicEventName) {
        this.publicEventName = publicEventName;
    }


    public String getPublicEventStartDate() {
        return publicEventStartDate;
    }

    public void setPublicEventStartDate(String publicEventStartDate) {
        this.publicEventStartDate = publicEventStartDate;
    }

    @UNIDIRECTIONAL
    @OneToOne(cascade = CascadeType.ALL)
    public Wall getPublicEventWall() {
        return publicEventWall;
    }

    public void setPublicEventWall(final Wall publicEventWall) {
        this.publicEventWall = publicEventWall;
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
    public PublicEvent refresh() throws DBFetchDataException {
        try {
        } catch (final Exception e__) {
            throw new DBFetchDataException(e__);
        }
        return this;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "PublicEvent{" +
                ", publicEventId=" + publicEventId +
                ", privateLocation=" + privateLocation +
                '}';
    }

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public PublicEvent setExtendedAccessR(Boolean extendedAccess) {
        setExtendedAccess(extendedAccess);
        return this;
    }

    @Transient
    public PublicEvent setLocationR(Location location) {
        setLocation(location);
        return this;
    }

    @Transient
    public PublicEvent setPublicEventEndDateR(String publicEventEndDate) {
        setPublicEventEndDate(publicEventEndDate);
        return this;
    }

    @Transient
    public PublicEvent setPublicEventIdR(final Long publicEventId) {
        setPublicEventId(publicEventId);
        return this;
    }

    @Transient
    public PublicEvent setPublicEventInfoR(String publicEventInfo) {
        setPublicEventInfo(publicEventInfo);
        return this;
    }

    @Transient
    public PublicEvent setPublicEventNameR(String publicEventName) {
        setPublicEventName(publicEventName);
        return this;
    }

    @Transient
    public PublicEvent setPublicEventStartDateR(String publicEventStartDate) {
        setPublicEventStartDate(publicEventStartDate);
        return this;
    }

    @Transient
    public PublicEvent setPublicEventWallR(final Wall publicEventWall) {
        this.publicEventWall = publicEventWall;
        return this;
    }

    @Transient
    public PublicEvent setPrivateLocationR(PrivateLocation privateLocation) {
        this.privateLocation = privateLocation;
        return this;
    }
}

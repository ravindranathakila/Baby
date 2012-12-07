package ai.ilikeplaces.entities;

import ai.ilikeplaces.entities.etc.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Important, if locationName & locationSuperSet of two objects are equal,
 * then the two are talking about the same location. They should be merged.
 * i.e. Made to have the same id.
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "Location", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@NamedQueries({
        @NamedQuery(name = "FindAllLocationsByName",
                query = "SELECT loc FROM Location loc WHERE loc.locationName = :locationName"),
        @NamedQuery(name = "FindAllLocationsBySuperLocation",
                query = "SELECT loc FROM " + Location.LocationEntity + " loc WHERE loc." + Location.LocationSuperSet + " = :" + Location.LocationSuperSet),
        @NamedQuery(name = "FindAllLocationNamesByLikeName",
                query = "SELECT loc.locationName FROM Location loc "),
        @NamedQuery(name = "FindAllLocationsByLikeName",
                query = "SELECT loc FROM Location loc")})
@EntityListeners({EntityLifeCycleListener.class})
public class Location implements Serializable, Clearance, Comparable<Location>, Refreshable<Location> {
// ------------------------------ FIELDS ------------------------------

    final static public String LocationEntity = "Location";
    final static public String FindAllLocationsByName = "FindAllLocationsByName";
    final static public String FindAllLocationsBySuperLocation = "FindAllLocationsBySuperLocation";
    final static public String FindAllLocationNamesByLikeName = "FindAllLocationNamesByLikeName";
    final static public String FindAllLocationsByLikeName = "FindAllLocationsByLikeName";
    final static public String LocationName = "locationName";
    final static public String LocationSuperSet = "locationSuperSet";
    final static public String WOEID = "WOEID";

    public static final String OF_SPACE = " of ";
    public static final String OF_SCORE = "_of_";

    final static private long serialVersionUID = 1L;

    private static final Refresh<Location> REFRESH = new Refresh<Location>();


    @Id
    @Column(name = "locationId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long locationId;
    public static final String locationIdCOL = "locationId";

    @Column(name = "clearance")
    public Long clearance = 0L;

    @Column(name = "locationName", unique = false, nullable = false)
    public String locationName;

    @Column(name = "locationInfo", unique = false, nullable = false, length = 1000)
    public String locationInfo;


    @OneToOne(mappedBy = "locationId", optional = false,
            targetEntity = Location.class)
    public Location locationSuperSet;

    @Column(name = "locationGeo1", length = 63)
    public String locationGeo1;

    @Column(name = "locationGeo2", length = 63)
    public String locationGeo2;

    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @OneToMany(mappedBy = PublicPhoto.locationCOL, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = locationIdCOL)
    public List<PublicPhoto> publicPhotos;


    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @OneToMany(mappedBy = PrivateEvent.locationCOL,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = locationIdCOL)
    public List<PrivateEvent> privateEvents;

    @RefreshId("longMsgs")
    @_unidirectional
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = locationIdCOL)
    public List<LongMsg> longMsgs;

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public Long getClearance() {
        return clearance;
    }

    @Override
    public void setClearance(final Long clearance) {
        this.clearance = clearance;
    }

    public String getLocationGeo1() {
        return locationGeo1;
    }

    public void setLocationGeo1(String locationGeo1) {
        this.locationGeo1 = locationGeo1;
    }

    public String getLocationGeo2() {
        return locationGeo2;
    }

    public void setLocationGeo2(String locationGeo2) {
        this.locationGeo2 = locationGeo2;
    }

    /**
     * @return locationId
     */
    public Long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId__
     */
    public void setLocationId(final Long locationId__) {
        this.locationId = locationId__;
    }

    /**
     * Important, if locationName & locationSuperSet of two objects are equal,
     * then the two are talking about the same location. They should be merged.
     * i.e. Made to have the same id.
     *
     * @return locationInfo
     */
    public String getLocationInfo() {
        return locationInfo;
    }

    /**
     * @param locationInfo__
     */
    public void setLocationInfo(final String locationInfo__) {
        this.locationInfo = locationInfo__;
    }

    /**
     * @return locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Important, if locationName & locationSuperSet of two objects are equal,
     * then the two are talking about the same location. They should be merged.
     * i.e. Made to have the same id.
     *
     * @param locationName__
     */
    public void setLocationName(final String locationName__) {
        this.locationName = locationName__;
    }

    /**
     * @return locationSuperSet
     */
    public Location getLocationSuperSet() {
        return locationSuperSet;
    }

    /**
     * @param locationSuperSet__
     */
    public void setLocationSuperSet(final Location locationSuperSet__) {
        this.locationSuperSet = locationSuperSet__;
    }

    public List<LongMsg> getLongMsgs() {
        return longMsgs;
    }

    public void setLongMsgs(List<LongMsg> longMsgs) {
        this.longMsgs = longMsgs;
    }

    public List<PrivateEvent> getPrivateEvents() {
        return privateEvents;
    }

    public void setPrivateEvents(List<PrivateEvent> privateEvents) {
        this.privateEvents = privateEvents;
    }

    @_fix(issue = "Break Location table by this field or use lazy fetching(better). i.e. LocationsPublicPhoto(P.K. = locationId), as that will ease transaction. Use secondary tables PrimaryKeyJoinTable")
    public List<PublicPhoto> getPublicPhotos() {
        return publicPhotos;
    }

    public void setPublicPhotos(List<PublicPhoto> publicPhotos) {
        this.publicPhotos = publicPhotos;
    }

// ------------------------ CANONICAL METHODS ------------------------

    /**
     * @return toString_
     */
    @Override
    @WARNING(warning = "The first location i.e. The Planet Earth, will have itself as its super. Hence will induce a stack overflow if superset.tostring is called.")
    public String toString() {
        return locationName + ((locationSuperSet == null || this.getLocationId().equals(locationSuperSet.getLocationId()) || this.getLocationId() == 1) ? "" : OF_SPACE + locationSuperSet.toString());
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Comparable ---------------------


    @Override
    public int compareTo(Location o) {
        return this.toString().compareTo(o.toString());
    }

// --------------------- Interface Refreshable ---------------------

    @Override
    public Location refresh(final RefreshSpec refreshSpec) throws RefreshException {
        REFRESH.refresh(this, refreshSpec);
        return this;
    }

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public Long getWOEID() {
        return locationId;
    }
}

package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@OK
@Entity
@EntityListeners(EntityLifeCycleListener.class)
@NamedQueries({
        @NamedQuery(name = "FindAllLocationsByName",
                query = "SELECT loc FROM Location loc WHERE loc.locationName = :locationName"),
        @NamedQuery(name = "FindAllLocationsBySuperLocation",
                query = "SELECT loc FROM " + Location.LocationEntity + " loc WHERE loc." + Location.LocationSuperSet + " = :" + Location.LocationSuperSet),
        @NamedQuery(name = "FindAllLocationNamesByLikeName",
                query = "SELECT loc.locationName FROM Location loc WHERE UPPER(loc.locationName) LIKE :locationName"),
        @NamedQuery(name = "FindAllLocationsByLikeName",
                query = "SELECT loc FROM Location loc WHERE UPPER(loc.locationName) LIKE :locationName")})
public class Location implements Serializable, Clearance, Comparable<Location> {

    final static Logger logger = LoggerFactory.getLogger(Location.class.getName());
    final static public String LocationEntity = "Location";
    final static public String FindAllLocationsByName = "FindAllLocationsByName";
    final static public String FindAllLocationsBySuperLocation = "FindAllLocationsBySuperLocation";
    final static public String FindAllLocationNamesByLikeName = "FindAllLocationNamesByLikeName";
    final static public String FindAllLocationsByLikeName = "FindAllLocationsByLikeName";
    final static public String LocationName = "locationName";
    final static public String LocationSuperSet = "locationSuperSet";
    final static private long serialVersionUID = 1L;

    private Long locationId;
    private Long clearance = 0L;
    private String locationName;
    private String locationInfo;
    private Location locationSuperSet;
    private String locationGeo1;
    private String locationGeo2;
    private List<PublicPhoto> publicPhotos;
    private List<PrivateEvent> privateEvents;


    /**
     * @return locationId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId__
     */
    public void setLocationId(final Long locationId__) {
        this.locationId = locationId__;
    }

    @Override
    public Long getClearance() {
        return clearance;
    }

    @Override
    public void setClearance(final Long clearance) {
        this.clearance = clearance;
    }

    /**
     * @return locationName
     */
    @Column(unique = false, nullable = false)
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
     * Important, if locationName & locationSuperSet of two objects are equal,
     * then the two are talking about the same location. They should be merged.
     * i.e. Made to have the same id.
     *
     * @return locationInfo
     */
    @Column(unique = false, nullable = false, length = 1000)
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
     * @return locationSuperSet
     */
    @OneToOne(optional = false,
            targetEntity = Location.class)
    public Location getLocationSuperSet() {
        return locationSuperSet;
    }

    /**
     * @param locationSuperSet__
     */
    public void setLocationSuperSet(final Location locationSuperSet__) {
        this.locationSuperSet = locationSuperSet__;
    }

    @FIXME(issue = "Break Location table by this field or use lazy fetching(better). i.e. LocationsPublicPhoto(P.K. = locationId), as that will ease transaction. Use secondary tables PrimaryKeyJoinTable")
    @OneToMany(mappedBy = PublicPhoto.locationCOL, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public List<PublicPhoto> getPublicPhotos() {
        return publicPhotos;
    }

    public void setPublicPhotos(List<PublicPhoto> publicPhotos) {
        this.publicPhotos = publicPhotos;
    }

    @Column(length = 63)
    public String getLocationGeo1() {
        return locationGeo1;
    }

    public void setLocationGeo1(String locationGeo1) {
        this.locationGeo1 = locationGeo1;
    }

    @Column(length = 63)
    public String getLocationGeo2() {
        return locationGeo2;
    }

    public void setLocationGeo2(String locationGeo2) {
        this.locationGeo2 = locationGeo2;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @OneToMany(mappedBy = PrivateEvent.locationCOL,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    public List<PrivateEvent> getPrivateEvents() {
        return privateEvents;
    }

    public void setPrivateEvents(List<PrivateEvent> privateEvents) {
        this.privateEvents = privateEvents;
    }

    /**
     * @return toString_
     */
    @Override
    @WARNING(warning = "The first location i.e. The Planet Earth, will have itself as its super. Hence will induce a stack overflow if superset.tostring is called.")
    public String toString() {
        return locationName + ((locationSuperSet == null || this.getLocationId() == locationSuperSet.getLocationId()) ? "" : " of " + locationSuperSet.toString());
    }

    @Override
    public int compareTo(Location o) {
        return this.toString().compareTo(o.toString());
    }
}

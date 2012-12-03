package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.doc._bidirectional;
import ai.ilikeplaces.entities.etc.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
@Table(name = "PrivateLocation", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
@NamedQueries({
        @NamedQuery(name = "FindAllPrivateLocationsByName",
                query = "SELECT loc FROM PrivateLocation loc"),
        @NamedQuery(name = "FindAllPrivateLocationNamesByLikeName",
                query = "SELECT loc.privateLocationName FROM PrivateLocation loc"),
        @NamedQuery(name = "FindAllPrivateLocationsByBounds",
                query = "SELECT loc FROM PrivateLocation loc")})
//                           select *   FROM ilp.privatelocation WHERE (    privatelocationlatitude BETWEEN                                 40                         AND                                   50                      ) AND (    privatelocationlongitude between                                 -75                        and                               -7                          )
public class PrivateLocation implements Serializable, RefreshData<PrivateLocation>, Refreshable<PrivateLocation> {
// ------------------------------ FIELDS ------------------------------

    final static public String FindAllPrivateLocationsByName = "FindAllPrivateLocationsByName";
    final static public String FindAllPrivateLocationNamesByLikeName = "FindAllPrivateLocationNamesByLikeName";
    final static public String FindAllPrivateLocationsByBounds = "FindAllPrivateLocationsByBounds";
    final static public String PrivateLocationName = "privateLocationName";

    public static final String PrivateLocationLatitudeSouth = "privateLocationLatitudeSouth";
    public static final String PrivateLocationLatitudeNorth = "privateLocationLatitudeNorth";
    public static final String PrivateLocationLongitudeWest = "privateLocationLongitudeWest";
    public static final String PrivateLocationLongitudeEast = "privateLocationLongitudeEast";

    private static final String PRIVATE_LOCATION = "PrivateLocation{";
    private static final String PRIVATE_LOCATION_ID = "privateLocationId=";
    private static final String LATITUDE = ", latitude=";
    private static final String LONGITUDE = ", longitude=";
    private static final char CLOSECURL = '}';

    private static final Refresh<PrivateLocation> REFRESH = new Refresh<PrivateLocation>();


    @Id
    @Column(name = "privateLocationId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long privateLocationId;
    public static final String privateLocationIdCOL = "privateLocationId";

    @Column(name = "privateLocationName", unique = false, nullable = false, length = 255)
    public String privateLocationName;

    @Column(name = "privateLocationInfo", nullable = false, length = 1000)
    public String privateLocationInfo;

    @Column(name = "privateLocationLatitude")
    public Double privateLocationLatitude;

    @Column(name = "privateLocationLongitude")
    public Double privateLocationLongitude;

    /**
     * Usually other members having rights to that place.
     * They cannot delete the location though. Only the creator can.
     */

    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @WARNING(warning = "Owning as deleting a location should automatically reflect in humans, not vice versa.")
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(
            name = privateLocationOwnersCOL + HumansPrivateLocation.privateLocationsOwnedCOL,
            joinColumns = @JoinColumn(name = privateLocationIdCOL),
            inverseJoinColumns = @JoinColumn(name = HumansPrivateLocation.humanIdCOL)
    )
    public List<HumansPrivateLocation> privateLocationOwners;
    final static public String privateLocationOwnersCOL = "privateLocationOwners";


    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @WARNING(warning = "Owning as deleting a location should automatically reflect in humans, not vice versa.")
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(
            name = privateLocationViewersCOL + HumansPrivateLocation.privateLocationsViewedCOL,
            joinColumns = @JoinColumn(name = privateLocationIdCOL),
            inverseJoinColumns = @JoinColumn(name = HumansPrivateLocation.humanIdCOL)
    )
    public List<HumansPrivateLocation> privateLocationViewers;
    final static public String privateLocationViewersCOL = "privateLocationViewers";

    /*All events are associated to a location*/
    /*No cascade ALL as we have to return an instance to the user, hence list cascading not possible.*/
    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @OneToMany(
            mappedBy = PrivateEvent.privateLocationCOL,
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    public List<PrivateEvent> privateEvents;

// --------------------- GETTER / SETTER METHODS ---------------------

    public List<PrivateEvent> getPrivateEvents() {
        return privateEvents;
    }

    public void setPrivateEvents(final List<PrivateEvent> privateEvents) {
        this.privateEvents = privateEvents;
    }

    /**
     * @return privateLocationId
     */
    public Long getPrivateLocationId() {
        return privateLocationId;
    }

    /**
     * @param privateLocationId__
     */
    public void setPrivateLocationId(final Long privateLocationId__) {
        this.privateLocationId = privateLocationId__;
    }

    /**
     * Important, if privateLocationName & privateLocationSuperSet of two objects are equal,
     * then the two are talking about the same privateLocation. They should be merged.
     * i.e. Made to have the same id.
     *
     * @return privateLocationInfo
     */
    public String getPrivateLocationInfo() {
        return privateLocationInfo;
    }

    /**
     * @param privateLocationInfo__
     */
    public void setPrivateLocationInfo(final String privateLocationInfo__) {
        this.privateLocationInfo = privateLocationInfo__;
    }

    public Double getPrivateLocationLatitude() {
        return privateLocationLatitude;
    }

    public void setPrivateLocationLatitude(final Double privateLocationLatitude_) {
        this.privateLocationLatitude = privateLocationLatitude_;
    }

    public Double getPrivateLocationLongitude() {
        return privateLocationLongitude;
    }

    public void setPrivateLocationLongitude(final Double privateLocationLongitude_) {
        this.privateLocationLongitude = privateLocationLongitude_;
    }

    /**
     * @return privateLocationName
     */
    public String getPrivateLocationName() {
        return privateLocationName;
    }

    /**
     * @param privateLocationName__
     */
    public void setPrivateLocationName(final String privateLocationName__) {
        this.privateLocationName = privateLocationName__;
    }

    public List<HumansPrivateLocation> getPrivateLocationOwners() {
        return privateLocationOwners;
    }

    public void setPrivateLocationOwners(final List<HumansPrivateLocation> privateLocationOwners) {
        this.privateLocationOwners = privateLocationOwners;
    }

    public List<HumansPrivateLocation> getPrivateLocationViewers() {
        return privateLocationViewers;
    }

    public void setPrivateLocationViewers(List<HumansPrivateLocation> privateLocationViewers) {
        this.privateLocationViewers = privateLocationViewers;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return PRIVATE_LOCATION +
                PRIVATE_LOCATION_ID + privateLocationId +
                LATITUDE + privateLocationLatitude +
                LONGITUDE + privateLocationLongitude +
                CLOSECURL;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface RefreshData ---------------------

    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @throws ai.ilikeplaces.entities.etc.DBRefreshDataException
     *
     */
    @Override
    public PrivateLocation refresh() throws DBRefreshDataException {
        this.getPrivateEvents().size();
        this.getPrivateLocationOwners().size();
        this.getPrivateLocationViewers().size();
        return this;
    }

// --------------------- Interface Refreshable ---------------------


    @Override
    public PrivateLocation refresh(final RefreshSpec refreshSpec) throws RefreshException {
        REFRESH.refresh(this, refreshSpec);
        return this;
    }
}


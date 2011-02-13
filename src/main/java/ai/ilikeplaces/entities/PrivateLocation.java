package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.jpa.Refresh;
import ai.ilikeplaces.util.jpa.RefreshException;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import ai.ilikeplaces.util.jpa.Refreshable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
@Entity
@EntityListeners(EntityLifeCycleListener.class)
@NamedQueries({
        @NamedQuery(name = "FindAllPrivateLocationsByName",
                query = "SELECT loc FROM PrivateLocation loc WHERE loc.privateLocationName = :privateLocationName"),
        @NamedQuery(name = "FindAllPrivateLocationNamesByLikeName",
                query = "SELECT loc.privateLocationName FROM PrivateLocation loc WHERE UPPER(loc.privateLocationName) LIKE :privateLocationName"),
        @NamedQuery(name = "FindAllPrivateLocationsByBounds",
                query = "SELECT loc FROM PrivateLocation loc WHERE (loc.privateLocationLatitude BETWEEN " + ":" + PrivateLocation.PrivateLocationLatitudeSouth + " AND " + ":" + PrivateLocation.PrivateLocationLatitudeNorth + ") AND (loc.privateLocationLongitude BETWEEN " + ":" + PrivateLocation.PrivateLocationLongitudeWest + " AND " + ":" + PrivateLocation.PrivateLocationLongitudeEast + ")")})
//                           select *   FROM ilp.privatelocation WHERE (    privatelocationlatitude BETWEEN                                 40                         AND                                   50                      ) AND (    privatelocationlongitude between                                 -75                        and                               -7                          )
public class PrivateLocation implements Serializable, RefreshData<PrivateLocation>, Refreshable<PrivateLocation> {

    final static Logger logger = LoggerFactory.getLogger(PrivateLocation.class.getName());

    final static public String FindAllPrivateLocationsByName = "FindAllPrivateLocationsByName";
    final static public String FindAllPrivateLocationNamesByLikeName = "FindAllPrivateLocationNamesByLikeName";
    final static public String FindAllPrivateLocationsByBounds = "FindAllPrivateLocationsByBounds";
    final static public String PrivateLocationName = "privateLocationName";

    public static final String PrivateLocationLatitudeSouth = "privateLocationLatitudeSouth";
    public static final String PrivateLocationLatitudeNorth = "privateLocationLatitudeNorth";
    public static final String PrivateLocationLongitudeWest = "privateLocationLongitudeWest";
    public static final String PrivateLocationLongitudeEast = "privateLocationLongitudeEast";


    public Long privateLocationId;

    public String privateLocationName;
    public String privateLocationInfo;

    public Double privateLocationLatitude;
    public Double privateLocationLongitude;

    /**
     * Usually other members having rights to that place.
     * They cannot delete the location though. Only the creator can.
     */
    public List<HumansPrivateLocation> privateLocationOwners;
    final static public String privateLocationOwnersCOL = "privateLocationOwners";

    public List<HumansPrivateLocation> privateLocationViewers;
    final static public String privateLocationViewersCOL = "privateLocationViewers";

    public List<PrivateEvent> privateEvents;
    private static final String PRIVATE_LOCATION = "PrivateLocation{";
    private static final String PRIVATE_LOCATION_ID = "privateLocationId=";
    private static final String LATITUDE = ", latitude=";
    private static final String LONGITUDE = ", longitude=";
    private static final char CLOSECURL = '}';

    private static final Refresh<PrivateLocation> REFRESH = new Refresh<PrivateLocation>();

    /**
     * @return privateLocationId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
     * @return privateLocationName
     */
    @Column(unique = false, nullable = false, length = 255)
    public String getPrivateLocationName() {
        return privateLocationName;
    }

    /**
     * @param privateLocationName__
     */
    public void setPrivateLocationName(final String privateLocationName__) {
        this.privateLocationName = privateLocationName__;
    }

    /**
     * Important, if privateLocationName & privateLocationSuperSet of two objects are equal,
     * then the two are talking about the same privateLocation. They should be merged.
     * i.e. Made to have the same id.
     *
     * @return privateLocationInfo
     */
    @Column(nullable = false, length = 1000)
    public String getPrivateLocationInfo() {
        return privateLocationInfo;
    }

    /**
     * @param privateLocationInfo__
     */
    public void setPrivateLocationInfo(final String privateLocationInfo__) {
        this.privateLocationInfo = privateLocationInfo__;
    }

    @Column
    public Double getPrivateLocationLatitude() {
        return privateLocationLatitude;
    }

    public void setPrivateLocationLatitude(final Double privateLocationLatitude_) {
        this.privateLocationLatitude = privateLocationLatitude_;
    }

    @Column
    public Double getPrivateLocationLongitude() {
        return privateLocationLongitude;
    }

    public void setPrivateLocationLongitude(final Double privateLocationLongitude_) {
        this.privateLocationLongitude = privateLocationLongitude_;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @WARNING(warning = "Owning as deleting a location should automatically reflect in humans, not vice versa.")
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<HumansPrivateLocation> getPrivateLocationOwners() {
        return privateLocationOwners;
    }

    public void setPrivateLocationOwners(final List<HumansPrivateLocation> privateLocationOwners) {
        this.privateLocationOwners = privateLocationOwners;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @WARNING(warning = "Owning as deleting a location should automatically reflect in humans, not vice versa.")
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<HumansPrivateLocation> getPrivateLocationViewers() {
        return privateLocationViewers;
    }

    public void setPrivateLocationViewers(List<HumansPrivateLocation> privateLocationViewers) {
        this.privateLocationViewers = privateLocationViewers;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @OneToMany(
            mappedBy = PrivateEvent.privateLocationCOL,
            /*All events are associated to a location*/
            /*No cascade ALL as we have to return an instance to the user, hence list cascading not possible.*/
            cascade = {CascadeType.REFRESH, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    public List<PrivateEvent> getPrivateEvents() {
        return privateEvents;
    }

    public void setPrivateEvents(final List<PrivateEvent> privateEvents) {
        this.privateEvents = privateEvents;
    }


    @Override
    public String toString() {
        return PRIVATE_LOCATION +
                PRIVATE_LOCATION_ID + privateLocationId +
                LATITUDE + privateLocationLatitude +
                LONGITUDE + privateLocationLongitude +
                CLOSECURL;
    }

    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @throws ai.ilikeplaces.exception.DBFetchDataException
     *
     */
    @Override
    public PrivateLocation refresh() throws DBFetchDataException {
        this.getPrivateEvents().size();
        this.getPrivateLocationOwners().size();
        this.getPrivateLocationViewers().size();
        return this;
    }

    @Override
    public PrivateLocation refresh(final RefreshSpec refreshSpec) throws RefreshException {
        REFRESH.refresh(this, refreshSpec);
        return this;
    }
}


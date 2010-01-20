package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.UNIDIRECTIONAL;
import ai.ilikeplaces.util.EntityLifeCyleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Important, if privateLocationName & privateLocationSuperSet of two objects are equal,
 * then the two are talking about the same privateLocation. They should be merged.
 * i.e. Made to have the same id.
 *
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
@Entity
@EntityListeners(EntityLifeCyleListener.class)
@NamedQueries({
        @NamedQuery(name = "FindAllPrivateLocationsByName",
                query = "SELECT loc FROM PrivateLocation loc WHERE loc.privateLocationName = :privateLocationName"),
        @NamedQuery(name = "FindAllPrivateLocationNamesByLikeName",
                query = "SELECT loc.privateLocationName FROM PrivateLocation loc WHERE UPPER(loc.privateLocationName) LIKE :privateLocationName")})
public class PrivateLocation implements Serializable {

    final static Logger logger = LoggerFactory.getLogger(PrivateLocation.class.getName());

    final static public String FindAllPrivateLocationsByName = "FindAllPrivateLocationsByName";
    final static public String FindAllPrivateLocationNamesByLikeName = "FindAllPrivateLocationNamesByLikeName";
    final static public String PrivateLocationName = "privateLocationName";

    private Long privateLocationId;

    private String privateLocationName;
    private String privateLocationInfo;

    /**
     * Usually other members having rights to that place.
     * They cannot delete the location though. Only the creater can.
     */
    private List<HumansPrivateLocation> privateLocationOwners;

    private List<PrivateEvent> privateEvents;

    /**
     * @return privateLocationId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    @Column(unique = false, nullable = false)
    public String getPrivateLocationName() {
        return privateLocationName;
    }

    /**
     * Important, if privateLocationName & privateLocationSuperSet of two objects are equal,
     * then the two are talking about the same privateLocation. They should be merged.
     * i.e. Made to have the same id.
     *
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
    @Column(unique = false, nullable = false, length = 1000)
    public String getPrivateLocationInfo() {
        return privateLocationInfo;
    }

    /**
     * @param privateLocationInfo__
     */
    public void setPrivateLocationInfo(final String privateLocationInfo__) {
        this.privateLocationInfo = privateLocationInfo__;
    }

    @OneToMany(
            /**
             * (NO ALL||REMOVE)
             * Deleting a location should not delete a user
             * (NO PERSIST||MERGE)
             * Creating a location should not create a user
             */
            cascade = CascadeType.REFRESH)
    @UNIDIRECTIONAL
    public List<HumansPrivateLocation> getPrivateLocationOwners() {
        return privateLocationOwners;
    }

    public void setPrivateLocationOwners(final List<HumansPrivateLocation> privateLocationOwners) {
        this.privateLocationOwners = privateLocationOwners;
    }

    @OneToMany(
            /*All events are associated to a location*/
            cascade = CascadeType.ALL)
    @BIDIRECTIONAL 
    public List<PrivateEvent> getPrivateEvents() {
        return privateEvents;
    }

    public void setPrivateEvents(final List<PrivateEvent> privateEvents) {
        this.privateEvents = privateEvents;
    }

    @Override
    public String toString() {
        return "PrivateLocation{" +
                "privateLocationId=" + privateLocationId +
                ", privateLocationName='" + privateLocationName + '\'' +
                '}';
    }
}
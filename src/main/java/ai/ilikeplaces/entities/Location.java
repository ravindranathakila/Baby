package ai.ilikeplaces.entities;

import ai.ilikeplaces.util.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Important, if locationName & locationSuperSet of two objects are equal,
 * then the two are talking about the same location. They should be merged.
 * i.e. Made to have the same id.
 * @author Ravindranath Akila
 */
@Entity
@EntityListeners(EntityLifeCyleListener.class)
@NamedQueries(@NamedQuery(name = "FindAllLocationsByName",
query = "SELECT loc FROM Location loc WHERE loc.locationName = :locationName"))
public class Location implements Serializable {

    final Logger logger = LoggerFactory.getLogger(Location.class.getName());
    final static public String FindAllLocationsByName = "FindAllLocationsByName";
    final static public String LocationName = "locationName";
    final static private long serialVersionUID = 1L;
    private Long locationId;
    private String locationName;
    private String locationInfo;
    private Location locationSuperSet;
    private List<PublicPhoto> publicPhotos;

    /**
     *
     * @return locationId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getLocationId() {
        return locationId;
    }

    /**
     *
     * @param locationId__
     */
    public void setLocationId(final Long locationId__) {
        this.locationId = locationId__;
    }

    /**
     * 
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
     * @param locationName__
     */
    public void setLocationName(final String locationName__) {
        this.locationName = locationName__;
    }

    /**
     * Important, if locationName & locationSuperSet of two objects are equal,
     * then the two are talking about the same location. They should be merged.
     * i.e. Made to have the same id.
     * @return locationInfo
     */
    @Column(unique = false, nullable = false)
    public String getLocationInfo() {
        return locationInfo;
    }

    /**
     *
     * @param locationInfo__
     */
    public void setLocationInfo(final String locationInfo__) {
        this.locationInfo = locationInfo__;
    }

    /**
     *
     * @return locationSuperSet
     */
    @OneToOne(optional = true,
    targetEntity = Location.class)
    public Location getLocationSuperSet() {
        return locationSuperSet;
    }

    /**
     *
     * @param locationSuperSet__
     */
    public void setLocationSuperSet(final Location locationSuperSet__) {
        this.locationSuperSet = locationSuperSet__;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    public List<PublicPhoto> getPublicPhotos() {
        return publicPhotos;
    }

    public void setPublicPhotos(List<PublicPhoto> publicPhotos) {
        this.publicPhotos = publicPhotos;
    }

    /**
     *
     * @return toString_
     */
    @Override
    public String toString() {
/*        String toString_ = getClass().getName();
        try {
            final Field[] fields = {getClass().getDeclaredField("locationId"),
                getClass().getDeclaredField("locationName"),
                getClass().getDeclaredField("locationSuperSet")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (final IllegalArgumentException ex) {
                    logger.error( null, ex);
                } catch (final IllegalAccessException ex) {
                    logger.error( null, ex);
                }
            }
        } catch (final NoSuchFieldException ex) {
            logger.error( null, ex);
        } catch (final SecurityException ex) {
            logger.error( null, ex);
        }

        return toString_;*/
        return locationName;
    }

    /**
     * 
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090914 Added locationId \n";
        changeLog += "20090914 Added locationSuperSet \n";
        changeLog += "20090914 Added toString \n";
        changeLog += "20090914 Added NamedQuery \n";
        changeLog += "20090915 Added Basic Validation \n";
        changeLog += "20090915 Removed Basic Validation \n";
        changeLog += "20090915 Added Annotated Validation \n";
        changeLog += "20090924 Changed locationId type to Long from long \n";
        changeLog += "20090925 Changed Superset fetch type to lazy\n";
        changeLog += "20090925 Added logger as a class field\n";
        return showChangeLog__ ? changeLog : toString();
    }
}

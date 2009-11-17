package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.FieldPreamble;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.util.EntityLifeCyleListener;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

/**
 * @author Ravindranath Akila
 */
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class PublicPhoto implements Serializable {

    @NOTE(note = "PRE PERSISTED ENTITIES WILL HAVE NULL IDS. HENCE USING PRE PERSISTED IDS IS NOT PRACTICAL.")
    final private UUID uUID = UUID.randomUUID();
    private static final long serialVersionUID = 1L;
    private Long publicPhotoId = null;

    private String publicPhotoFilePath;

    @FieldPreamble(description = "The path should be very random as it will be exposed to the www." +
            "Also make sure this supports good SEO.")
    private String publicPhotoURLPath;

    private String publicPhotoName;

    private String publicPhotoDescription;

    @FieldPreamble(description = "Required to calculate ranking")
    private Date publicPhotoUploadDate;
    @FieldPreamble(description = "Required to calculate rank position")

    private Date publicPhotoTakenDate;
    @FieldPreamble(description = "Required to calculate rank position")
    private Long publicPhotoRankUnits;

    @FieldPreamble(description = "Required to calculate rank position")
    private Long publicPhotoRankTurns;

    @FieldPreamble(description = "Required when rebuilding a database from scratch someday." +
            "Since the whole concept of ilikeplaces relies on content richness, preserving this in this table important.")
    private Location location;

    @FieldPreamble(description = "Who uploaded this image? Wil he request to delete it? " +
            "Privacy important? " +
            "Lets preserve the info.")
    private HumansPublicPhoto humansPublicPhoto;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getPublicPhotoId() {
        return publicPhotoId;
    }

    public void setPublicPhotoId(Long publicPhotoId) {
        this.publicPhotoId = publicPhotoId;
    }

    public String getPublicPhotoFilePath() {
        return publicPhotoFilePath;
    }

    public void setPublicPhotoFilePath(String publicPhotoFilePath) {
        this.publicPhotoFilePath = publicPhotoFilePath;
    }

    public String getPublicPhotoName() {
        return publicPhotoName;
    }

    public void setPublicPhotoName(String publicPhotoName) {
        this.publicPhotoName = publicPhotoName;
    }

    public String getPublicPhotoDescription() {
        return publicPhotoDescription;
    }

    public void setPublicPhotoDescription(final String publicPhotoDescription) {
        this.publicPhotoDescription = publicPhotoDescription;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    public HumansPublicPhoto getHumansPublicPhoto() {
        return humansPublicPhoto;
    }

    public void setHumansPublicPhoto(HumansPublicPhoto humansPublicPhoto) {
        this.humansPublicPhoto = humansPublicPhoto;
    }

    @FIXME(issue = "Break Location table by this field. i.e. LocationsPublicPhoto(P.K. = locationId), as that will ease transaction")
    @ManyToOne(cascade = {CascadeType.MERGE})
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getPublicPhotoRankTurns() {
        return publicPhotoRankTurns;
    }

    public void setPublicPhotoRankTurns(Long publicPhotoRankTurns) {
        this.publicPhotoRankTurns = publicPhotoRankTurns;
    }

    public Long getPublicPhotoRankUnits() {
        return publicPhotoRankUnits;
    }

    public void setPublicPhotoRankUnits(Long publicPhotoRankUnits) {
        this.publicPhotoRankUnits = publicPhotoRankUnits;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPublicPhotoTakenDate() {
        return publicPhotoTakenDate;
    }

    public void setPublicPhotoTakenDate(Date publicPhotoTakenDate) {
        this.publicPhotoTakenDate = publicPhotoTakenDate;
    }

    public String getPublicPhotoURLPath() {
        return publicPhotoURLPath;
    }

    public void setPublicPhotoURLPath(String publicPhotoURLPath) {
        this.publicPhotoURLPath = publicPhotoURLPath;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPublicPhotoUploadDate() {
        return publicPhotoUploadDate;
    }

    public void setPublicPhotoUploadDate(Date publicPhotoUploadDate) {
        this.publicPhotoUploadDate = publicPhotoUploadDate;
    }

    @FIXME(issue = "VERIFY THAT UUID IS NOT RESOURCE INTENSIVE.")
    @Override
    public int hashCode() {
        return (int) (this.getPublicPhotoId() != null ? this.getPublicPhotoId() : uUID.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean returnVal = false;
        equals:
        {
            if (obj == null) {
                returnVal = false;
                break equals;
            }
            if (getClass() != obj.getClass()) {
                returnVal = false;
                break equals;
            }
            final PublicPhoto other = (PublicPhoto) obj;
            /*Are both not null?*/
            if (this.getPublicPhotoId() != null && other.getPublicPhotoId() != null) {
                if (this.getPublicPhotoId().equals(other.getPublicPhotoId())) {
                    returnVal = true;
                    break equals;
                }
            } else if (this.getPublicPhotoId() == null && other.getPublicPhotoId() == null) {/*Are both null?*/
                /*i.e. Still not persisted. Lets compare UUIDs*/
                if (this.uUID.equals(other.uUID)) {
                    returnVal = true;
                    break equals;
                } else {
                    returnVal = false;
                    break equals;
                }
            } else {/*Only one is null*/
                returnVal = false;
                break equals;
            }

        }
        return returnVal;
    }

    /**
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = new String(getClass().getName());
        try {
            final Field[] fields = {getClass().getDeclaredField("publicPhotoId")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    LoggerFactory.getLogger(Location.class.getName()).error(null, ex);
                } catch (IllegalAccessException ex) {
                    LoggerFactory.getLogger(Location.class.getName()).error(null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            LoggerFactory.getLogger(Location.class.getName()).error(null, ex);
        } catch (SecurityException ex) {
            LoggerFactory.getLogger(Location.class.getName()).error(null, ex);
        }

        return toString_;
    }

    /**
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090914 Added this class \n";
        return showChangeLog__ ? changeLog : toString();
    }
}

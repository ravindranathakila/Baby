package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.FieldPreamble;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Ravindranath Akila
 */
@Entity
public class PublicPhoto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long publicPhotoId;

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
    @GeneratedValue(strategy=GenerationType.AUTO)
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

    @ManyToOne
    public HumansPublicPhoto getHumansPublicPhoto() {
        return humansPublicPhoto;
    }

    public void setHumansPublicPhoto(HumansPublicPhoto humansPublicPhoto) {
        this.humansPublicPhoto = humansPublicPhoto;
    }

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
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



    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.publicPhotoId != null ? this.publicPhotoId.hashCode() : 0);
        hash = 37 * hash + (this.publicPhotoFilePath != null ? this.publicPhotoFilePath.hashCode() : 0);
        hash = 37 * hash + (this.publicPhotoURLPath != null ? this.publicPhotoURLPath.hashCode() : 0);
        return hash;
    }

    @Override
    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass", "EqualsWhichDoesntCheckParameterClass"})
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof PublicPhoto)) {
//            return false;
//        }
//        PublicPhoto other = (PublicPhoto) object;
//        if ((this.publicPhotoId == null && other.publicPhotoId != null) || (this.publicPhotoId != null && !this.publicPhotoId.equals(other.publicPhotoId))) {
//            return false;
//        }
//        return true;
        throw new UnsupportedOperationException("PLEASE PROVIDE THE BODY OF THIS EQUALS METHOD!");
    }

    /**
     *
     * @return toString_
     */
    @Override
    public String toString() {
        String toString_ = new String(getClass().getName());
        try {
            final Field[] fields = {getClass().getDeclaredField("locationId"),
                getClass().getDeclaredField("locationName"),
                getClass().getDeclaredField("locationSuperSet")};

            for (final Field field : fields) {
                try {
                    toString_ += "\n{" + field.getName() + "," + field.get(this) + "}";
                } catch (IllegalArgumentException ex) {
                    LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
                } catch (IllegalAccessException ex) {
                    LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
                }
            }
        } catch (NoSuchFieldException ex) {
            LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
        } catch (SecurityException ex) {
            LoggerFactory.getLogger(Location.class.getName()).error( null, ex);
        }

        return toString_;
    }

    /**
     *
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090914 Added this class \n";
        return showChangeLog__ ? changeLog : toString();
    }
}

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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Ravindranath Akila
 */
@Entity
public class PrivatePhoto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long privatePhotoId;
    @FieldPreamble(description = "Put in folders?" +
    "Folders might be renamed due to change in location names if you use location names in folders." +
    "Well having the location name in the file name is important for SEO. e.g. paris.jpg" +
    "Would an approach like {random_number}_location.jpg work?" +
    "Would an approach like {sequence_number}_location.jpg work?()")
    private String privatePhotoFilePath;
    @FieldPreamble(description = "The path should be very random as it will be exposed to the www." +
    "Also make sure this supports good SEO.")
    private String privatePhotoURLPath;

    private String privatePhotoName;
    
    private String privatePhotoDescription;
    @FieldPreamble(description = "Required to calculate ranking")
    private Date privatePhotoUploadDate;
    @FieldPreamble(description = "Required to calculate rank position")
    private Date privatePhotoTakenDate;
    @FieldPreamble(description = "Required when rebuilding a database from scratch someday." +
    "Since the whole concept of ilikeplaces relies on content richness, preserving this in this table important.")
    private Location privatePhotoOfLocation;
    @FieldPreamble(description = "Who uploaded this image? Wil he request to delete it? " +
    "Privacy important? " +
    "Lets preserve the info.")
    private HumansPrivatePhoto humansPrivatePhoto;
    private Location location;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getPrivatePhotoId() {
        return privatePhotoId;
    }

    public void setPrivatePhotoId(Long privatePhotoId) {
        this.privatePhotoId = privatePhotoId;
    }

    public String getPrivatePhotoName() {
        return privatePhotoName;
    }

    public void setPrivatePhotoName(String privatePhotoName) {
        this.privatePhotoName = privatePhotoName;
    }

    public String getPrivatePhotoDescription() {
        return privatePhotoDescription;
    }

    public void setPrivatePhotoDescription(final String privatePhotoDescription) {
        this.privatePhotoDescription = privatePhotoDescription;
    }

    @OneToOne
    public HumansPrivatePhoto getHumansPrivatePhoto() {
        return humansPrivatePhoto;
    }

    public void setHumansPrivatePhoto(HumansPrivatePhoto humansPrivatePhoto) {
        this.humansPrivatePhoto = humansPrivatePhoto;
    }

    public String getPrivatePhotoFilePath() {
        return privatePhotoFilePath;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPrivatePhotoFilePath(String privatePhotoFilePath) {
        this.privatePhotoFilePath = privatePhotoFilePath;
    }

    @ManyToOne
    public Location getPrivatePhotoOfLocation() {
        return privatePhotoOfLocation;
    }

    public void setPrivatePhotoOfLocation(Location privatePhotoOfLocation) {
        this.privatePhotoOfLocation = privatePhotoOfLocation;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPrivatePhotoTakenDate() {
        return privatePhotoTakenDate;
    }

    public void setPrivatePhotoTakenDate(Date privatePhotoTakenDate) {
        this.privatePhotoTakenDate = privatePhotoTakenDate;
    }

    public String getPrivatePhotoURLPath() {
        return privatePhotoURLPath;
    }

    public void setPrivatePhotoURLPath(String privatePhotoURLPath) {
        this.privatePhotoURLPath = privatePhotoURLPath;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPrivatePhotoUploadDate() {
        return privatePhotoUploadDate;
    }

    public void setPrivatePhotoUploadDate(Date privatePhotoUploadDate) {
        this.privatePhotoUploadDate = privatePhotoUploadDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.privatePhotoId != null ? this.privatePhotoId.hashCode() : 0);
        hash = 37 * hash + (this.privatePhotoFilePath != null ? this.privatePhotoFilePath.hashCode() : 0);
        hash = 37 * hash + (this.privatePhotoURLPath != null ? this.privatePhotoURLPath.hashCode() : 0);
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
//        if ((this.privatePhotoId == null && other.privatePhotoId != null) || (this.privatePhotoId != null && !this.privatePhotoId.equals(other.privatePhotoId))) {
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

package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.exception.PendingEqualsMethodException;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * @author Ravindranath Akila
 */

@Entity
public class PrivatePhoto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long privatePhotoId;

    @FieldPreamble(description = "CDN security issue. Put in folders?")
    private String privatePhotoFilePath;

    @FieldPreamble(description = "The path should be very random as it will be exposed to the www." +
            "Also make sure this supports good SEO.")
    private String privatePhotoURLPath;

    private String privatePhotoName;

    private String privatePhotoDescription;

    @FieldPreamble(description = "Required to show users")
    private Date privatePhotoUploadDate;

    @FieldPreamble(description = "Required to show users")
    private Date privatePhotoTakenDate;

    @FieldPreamble(description = "Who uploaded this image? Wil he request to delete it? " +
            "Privacy important? " +
            "Lets preserve the info.")
    private HumansPrivatePhoto humansPrivatePhoto;

    @NOTE(note = "Rank. Calculated by Referrer Ranking and Hits.")
    private long hits;

    private Location location;

    private List<Album> albums;
    final static public String albumsCol = "albums";

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

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @WARNING(warning = "Owning as deleting a photo should automatically reflect in albums, not vice versa.")
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
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
    @TODO(task = "DO AS IN PublicPhoto.class, UUID")
    public boolean equals(Object object) {
        throw new PendingEqualsMethodException();
    }

    /**
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

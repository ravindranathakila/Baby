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
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
public class PrivatePhoto implements Serializable {

    private static final long serialVersionUID = 1L;
    public Long privatePhotoId;

    @FieldPreamble(description = "CDN security issue. Put in folders?")
    public String privatePhotoFilePath;

    @FieldPreamble(description = "The path should be very random as it will be exposed to the www." +
            "Also make sure this supports good SEO.")
    public String privatePhotoURLPath;

    public String privatePhotoName;

    public String privatePhotoDescription;

    @FieldPreamble(description = "Required to show users")
    public Date privatePhotoUploadDate;

    @FieldPreamble(description = "Required to show users")
    public Date privatePhotoTakenDate;

    @FieldPreamble(description = "Who uploaded this image? Wil he request to delete it? " +
            "Privacy important? " +
            "Lets preserve the info.")
    public HumansPrivatePhoto humansPrivatePhoto;

    public List<Album> albums;
    final static public String albumsCol = "albums";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getPrivatePhotoId() {
        return privatePhotoId;
    }

    public void setPrivatePhotoId(Long privatePhotoId) {
        this.privatePhotoId = privatePhotoId;
    }

    @Transient
    public PrivatePhoto setPrivatePhotoIdR(Long privatePhotoId) {
        setPrivatePhotoId(privatePhotoId);
        return this;
    }

    public String getPrivatePhotoName() {
        return privatePhotoName;
    }

    public void setPrivatePhotoName(String privatePhotoName) {
        this.privatePhotoName = privatePhotoName;
    }

    @Transient
    public PrivatePhoto setPrivatePhotoNameR(String privatePhotoName) {
        setPrivatePhotoName(privatePhotoName);
        return this;
    }

    public String getPrivatePhotoDescription() {
        return privatePhotoDescription;
    }

    public void setPrivatePhotoDescription(final String privatePhotoDescription) {
        this.privatePhotoDescription = privatePhotoDescription;
    }

    @Transient
    public PrivatePhoto setPrivatePhotoDescriptionR(final String privatePhotoDescription) {
        setPrivatePhotoDescription(privatePhotoDescription);
        return this;
    }

    @OneToOne
    public HumansPrivatePhoto getHumansPrivatePhoto() {
        return humansPrivatePhoto;
    }

    public void setHumansPrivatePhoto(HumansPrivatePhoto humansPrivatePhoto) {
        this.humansPrivatePhoto = humansPrivatePhoto;
    }

    @Transient
    public PrivatePhoto setHumansPrivatePhotoR(HumansPrivatePhoto humansPrivatePhoto) {
        setHumansPrivatePhoto(humansPrivatePhoto);
        return this;
    }

    public String getPrivatePhotoFilePath() {
        return privatePhotoFilePath;
    }

    public void setPrivatePhotoFilePath(String privatePhotoFilePath) {
        this.privatePhotoFilePath = privatePhotoFilePath;
    }

    @Transient
    public PrivatePhoto setPrivatePhotoFilePathR(String privatePhotoFilePath) {
        setPrivatePhotoFilePath(privatePhotoFilePath);
        return this;
    }


    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPrivatePhotoTakenDate() {
        return privatePhotoTakenDate;
    }

    public void setPrivatePhotoTakenDate(Date privatePhotoTakenDate) {
        this.privatePhotoTakenDate = privatePhotoTakenDate;
    }

    @Transient
    public PrivatePhoto setPrivatePhotoTakenDateR(Date privatePhotoTakenDate) {
        setPrivatePhotoTakenDate(privatePhotoTakenDate);
        return this;
    }

    public String getPrivatePhotoURLPath() {
        return privatePhotoURLPath;
    }

    public void setPrivatePhotoURLPath(String privatePhotoURLPath) {
        this.privatePhotoURLPath = privatePhotoURLPath;
    }

    @Transient
    public PrivatePhoto setPrivatePhotoURLPathR(String privatePhotoURLPath) {
        setPrivatePhotoURLPath(privatePhotoURLPath);
        return this;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPrivatePhotoUploadDate() {
        return privatePhotoUploadDate;
    }

    public void setPrivatePhotoUploadDate(Date privatePhotoUploadDate) {
        this.privatePhotoUploadDate = privatePhotoUploadDate;
    }

    @Transient
    public PrivatePhoto setPrivatePhotoUploadDateR(Date privatePhotoUploadDate) {
        setPrivatePhotoUploadDate(privatePhotoUploadDate);
        return this;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.privatePhotoId != null ? this.privatePhotoId.hashCode() : 0);
        return hash;
    }

    @Override
    @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass", "EqualsWhichDoesntCheckParameterClass"})
    @TODO(task = "DO AS IN PrivatePhoto.class, UUID")
    public boolean equals(Object object) {
        throw PendingEqualsMethodException.SINGLETON;
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

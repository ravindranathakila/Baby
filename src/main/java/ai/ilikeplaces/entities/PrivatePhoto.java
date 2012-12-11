package ai.ilikeplaces.entities;

import ai.doc.*;
import ai.ilikeplaces.entities.etc.*;
import ai.ilikeplaces.logic.Listeners.widgets.WallWidgetPrivatePhoto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_doc(
        LOGIC = @_logic(
                SEE = @_see(WallWidgetPrivatePhoto.class)
        )
)
@Table(name = "PrivatePhoto", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class PrivatePhoto implements Serializable, Comparable<PrivatePhoto>, Refreshable<PrivatePhoto> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "privatePhotoId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long privatePhotoId;
    public static final String privatePhotoIdCOL = "privatePhotoId";

    @_field_preamble(description = "CDN security issue. Put in folders?")
    public String privatePhotoFilePath;

    @_field_preamble(description = "The path should be very random as it will be exposed to the www." +
            "Also make sure this supports good SEO.")
    @Column(name = "privatePhotoURLPath")
    public String privatePhotoURLPath;

    @Column(name = "privatePhotoName")
    public String privatePhotoName;

    @Column(name = "privatePhotoDescription")
    public String privatePhotoDescription;

    @_field_preamble(description = "Required to show users")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "privatePhotoUploadDate")
    public Date privatePhotoUploadDate;

    @_field_preamble(description = "Required to show users")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "privatePhotoTakenDate")
    public Date privatePhotoTakenDate;

    @_field_preamble(description = "Who uploaded this image? Wil he request to delete it? " +
            "Privacy important? " +
            "Lets preserve the info.")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = HumansPrivatePhoto.humanIdCOL)
    public HumansPrivatePhoto humansPrivatePhoto;

    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @WARNING(warning = "Owning as deleting a photo should automatically reflect in albums, not vice versa.")
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(
            name = albumsCOL + Album.albumPhotosCOL,
            joinColumns = @JoinColumn(name = privatePhotoIdCOL),
            inverseJoinColumns = @JoinColumn(name = Album.albumIdCOL)
    )
    public List<Album> albums;
    final static public String albumsCOL = "albums";

    @RefreshId("privatePhotoWall")
    @_unidirectional
    @OneToOne(mappedBy = "wallId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Wall privatePhotoWall;

    private static final Refresh<PrivatePhoto> REFRESH = new Refresh<PrivatePhoto>();

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

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public Wall getPrivatePhotoWall() {
        return privatePhotoWall;
    }

    public void setPrivatePhotoWall(Wall privatePhotoWall) {
        this.privatePhotoWall = privatePhotoWall;
    }

    public PrivatePhoto setPrivatePhotoWallR(Wall privatePhotoWall) {
        this.privatePhotoWall = privatePhotoWall;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.privatePhotoId != null ? this.privatePhotoId.hashCode() : 0);
        return hash;
    }


    /**
     * @param showChangeLog__
     * @return changeLog
     */
    @Override
    public String toString() {
        return "PrivatePhoto{" +
                "privatePhotoId=" + privatePhotoId +
                ", privatePhotoFilePath='" + privatePhotoFilePath + '\'' +
                ", privatePhotoURLPath='" + privatePhotoURLPath + '\'' +
                ", privatePhotoName='" + privatePhotoName + '\'' +
                ", privatePhotoDescription='" + privatePhotoDescription + '\'' +
                ", privatePhotoUploadDate=" + privatePhotoUploadDate +
                ", privatePhotoTakenDate=" + privatePhotoTakenDate +
                ", humansPrivatePhoto=" + humansPrivatePhoto +
                ", albums=" + albums +
                ", privatePhotoWall=" + privatePhotoWall +
                '}';
    }


    /**
     * @param toBeComparedWith Object To Be ComparedWith
     * @return curent - toBeComparedWith
     */
    @Override
    public int compareTo(final PrivatePhoto toBeComparedWith) {
        return (int) (this.getPrivatePhotoId() - toBeComparedWith.getPrivatePhotoId());
    }

    @Override
    public PrivatePhoto refresh(final RefreshSpec refreshSpec) throws RefreshException {
        REFRESH.refresh(this, refreshSpec);
        return this;
    }
}

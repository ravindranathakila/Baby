package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.doc._bidirectional;
import ai.ilikeplaces.entities.etc.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 25, 2010
 * Time: 1:01:22 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "Album", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Album implements RefreshData<Album>, Refreshable<Album>, Serializable {


    @Id
    @Column(name = "albumId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long albumId;
    public static final String albumIdCOL = "albumId";

    @Column(name = "albumName", length = 255)
    public String albumName;

    @Column(name = "albumDescription", length = 1023)
    public String albumDescription;


    @RefreshId("albumPhotos")

    @WARNING(warning = "Not owner because when a photo is deleted, the albums will automatically reflect it." +
            "The other way round is not feasible because a user will own photos, not albums.")
    @NOTE(note = "ManyToMany because photos can be moved to a different album when deleting events.")
    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @ManyToMany(mappedBy = PrivatePhoto.albumsCOL, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<PrivatePhoto> albumPhotos;
    final static public String albumPhotosCOL = "albumPhotos";

    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = Album.albumIdCOL)
    public List<HumansAlbum> albumOwners;
    final static public String albumOwnersCOL = "albumOwners";


    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public List<HumansAlbum> albumVisitors;
    @JoinColumn(name = Album.albumIdCOL)
    final static public String albumVisitorsCOL = "albumVisitors";


    @_bidirectional(ownerside = _bidirectional.OWNING.NOT)
    @OneToOne(mappedBy = PrivateEvent.privateEventAlbumCOL, fetch = FetchType.EAGER)
    public PrivateEvent albumPrivateEvent;

    public static final Refresh<Album> REFRESH = new Refresh<Album>();

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(final Long albumId) {
        this.albumId = albumId;
    }

    @Transient
    public Album setAlbumIdR(final Long albumId) {
        setAlbumId(albumId);
        return this;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(final String albumDescription) {
        this.albumDescription = albumDescription;
    }

    @Transient
    public Album setAlbumDescriptionR(final String albumDescription) {
        this.albumDescription = albumDescription;
        return this;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(final String albumName) {
        this.albumName = albumName;
    }

    @Transient
    public Album setAlbumNameR(final String albumName) {
        this.albumName = albumName;
        return this;
    }


    public PrivateEvent getAlbumPrivateEvent() {
        return albumPrivateEvent;
    }

    public void setAlbumPrivateEvent(PrivateEvent albumPrivateEvent) {
        this.albumPrivateEvent = albumPrivateEvent;
    }

    public List<HumansAlbum> getAlbumOwners() {
        return albumOwners;
    }

    public void setAlbumOwners(final List<HumansAlbum> albumOwners) {
        this.albumOwners = albumOwners;
    }

    public List<PrivatePhoto> getAlbumPhotos() {
        return albumPhotos;
    }

    public void setAlbumPhotos(final List<PrivatePhoto> albumPhotos) {
        this.albumPhotos = albumPhotos;
    }


    public List<HumansAlbum> getAlbumVisitors() {
        return albumVisitors;
    }

    public void setAlbumVisitors(final List<HumansAlbum> albumVisitors) {
        this.albumVisitors = albumVisitors;
    }

    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @throws DBRefreshDataException
     */
    @Override
    public Album refresh() throws DBRefreshDataException {
        try {
            this.getAlbumPhotos().size();
            this.getAlbumOwners().size();
            this.getAlbumVisitors().size();
        } catch (final Exception e) {
            throw new DBRefreshDataException(e);
        }
        return this;
    }

    @Override
    public Album refresh(final RefreshSpec refreshSpec) throws RefreshException {
        REFRESH.refresh(this, refreshSpec);
        return this;
    }
}

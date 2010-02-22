package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.util.EntityLifeCycleListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jan 25, 2010
 * Time: 1:01:22 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Album {

    private Long albumId;

    private String albumName;

    private String albumDescription;

    private List<PrivatePhoto> albumPhotos;

    private List<HumansAlbum> albumOwners;
    final static public String albumOwnersCOL = "albumOwners";

    private List<HumansAlbum> albumVisitors;
    final static public String albumVisitorsCOL = "albumVisitors";

    private PrivateEvent privateEvent;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(final Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    @Column(length = 1023)
    public void setAlbumDescription(final String albumDescription) {
        this.albumDescription = albumDescription;
    }

    public String getAlbumName() {
        return albumName;
    }

    @Column(length = 255)
    public void setAlbumName(final String albumName) {
        this.albumName = albumName;
    }

    public PrivateEvent getPrivateEvent() {
        return privateEvent;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @OneToOne(mappedBy = PrivateEvent.albumCOL, fetch = FetchType.EAGER)
    public void setPrivateEvent(PrivateEvent privateEvent) {
        this.privateEvent = privateEvent;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<HumansAlbum> getAlbumOwners() {
        return albumOwners;
    }

    public void setAlbumOwners(final List<HumansAlbum> albumOwners) {
        this.albumOwners = albumOwners;
    }

    @WARNING(warning = "Not owner because when a photo is deleted, the albums will automatically reflect it." +
            "The other way round is not feasible because a user will photos, not albums.")
    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(mappedBy = PrivatePhoto.albumsCol, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivatePhoto> getAlbumPhotos() {
        return albumPhotos;
    }

    public void setAlbumPhotos(final List<PrivatePhoto> albumPhotos) {
        this.albumPhotos = albumPhotos;
    }


    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.IS)
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<HumansAlbum> getAlbumVisitors() {
        return albumVisitors;
    }

    public void setAlbumVisitors(final List<HumansAlbum> albumVisitors) {
        this.albumVisitors = albumVisitors;
    }
}

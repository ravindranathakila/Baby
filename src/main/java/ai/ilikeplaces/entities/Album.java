package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.UNIDIRECTIONAL;
import ai.ilikeplaces.util.EntityLifeCyleListener;

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
@EntityListeners({EntityLifeCyleListener.class})
public class Album {

    private Long albumId;

    private String albumName;

    private String albumDescription;

    private List<PrivatePhoto> albumPhotos;

    private List<Human> albumOwners;

    private List<Human> albumVisitors;

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

    @UNIDIRECTIONAL
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<Human> getAlbumOwners() {
        return albumOwners;
    }

    public void setAlbumOwners(final List<Human> albumOwners) {
        this.albumOwners = albumOwners;
    }

    @UNIDIRECTIONAL
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivatePhoto> getAlbumPhotos() {
        return albumPhotos;
    }

    public void setAlbumPhotos(final List<PrivatePhoto> albumPhotos) {
        this.albumPhotos = albumPhotos;
    }


    @UNIDIRECTIONAL
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<Human> getAlbumVisitors() {
        return albumVisitors;
    }

    public void setAlbumVisitors(final List<Human> albumVisitors) {
        this.albumVisitors = albumVisitors;
    }
}

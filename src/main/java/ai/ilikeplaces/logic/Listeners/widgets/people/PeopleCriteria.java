package ai.ilikeplaces.logic.Listeners.widgets.people;

import ai.ilikeplaces.entities.PrivatePhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:48 PM
 */
public class PeopleCriteria {
    private List<PrivatePhoto> albumPhotos = null;

    public PeopleCriteria setAlbumPhotos(List<PrivatePhoto> albumPhotos) {
        this.albumPhotos = albumPhotos;
        return this;
    }

    public List<PrivatePhoto> getAlbumPhotos() {
        return albumPhotos != null ? albumPhotos : (albumPhotos = new ArrayList<PrivatePhoto>(0));
    }
}

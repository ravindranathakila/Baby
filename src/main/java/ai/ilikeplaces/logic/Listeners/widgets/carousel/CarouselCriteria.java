package ai.ilikeplaces.logic.Listeners.widgets.carousel;

import ai.ilikeplaces.entities.PrivatePhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:48 PM
 */
public class CarouselCriteria {
    private List<PrivatePhoto> albumPhotos = null;

    public CarouselCriteria setAlbumPhotos(List<PrivatePhoto> albumPhotos) {
        this.albumPhotos = albumPhotos;
        return this;
    }

    public List<PrivatePhoto> getAlbumPhotos() {
        return albumPhotos != null ? albumPhotos : (albumPhotos = new ArrayList<PrivatePhoto>(0));
    }
}

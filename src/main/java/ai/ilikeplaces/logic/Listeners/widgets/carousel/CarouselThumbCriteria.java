package ai.ilikeplaces.logic.Listeners.widgets.carousel;

import ai.doc.WARNING;
import ai.ilikeplaces.entities.PrivatePhoto;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:48 PM
 */
public class CarouselThumbCriteria {

    private PrivatePhoto privatePhoto = null;

    public CarouselThumbCriteria setPrivatePhoto(final PrivatePhoto privatePhoto) {
        this.privatePhoto = privatePhoto;
        return this;
    }

    @WARNING("Prone to NPE since in the getter we cannot create a new object as we don't know how to do so")
    public PrivatePhoto getPrivatePhoto() {
        return privatePhoto;
    }
}

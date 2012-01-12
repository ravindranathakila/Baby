package ai.ilikeplaces.logic.Listeners.widgets.people;

import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.PrivatePhoto;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:48 PM
 */
public class PeopleThumbCriteria {

    private PrivatePhoto privatePhoto = null;

    public PeopleThumbCriteria setPrivatePhoto(final PrivatePhoto privatePhoto) {
        this.privatePhoto = privatePhoto;
        return this;
    }

    @WARNING("Prone to NPE since in the getter we cannot create a new object as we don't know how to do so")
    public PrivatePhoto getPrivatePhoto() {
        return privatePhoto;
    }
}

package ai.ilikeplaces.logic.Listeners.widgets.people;

import ai.doc.WARNING;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/1/12
 * Time: 12:48 PM
 */
public class PeopleThumbCriteria {

    private String profilePhoto = null;

    public PeopleThumbCriteria setProfilePhoto(final String profilePhoto) {
        this.profilePhoto = profilePhoto;
        return this;
    }

    @WARNING("Prone to NPE since in the getter we cannot create a new object as we don't know how to do so")
    public String getProfilePhoto() {
        return profilePhoto;
    }
}

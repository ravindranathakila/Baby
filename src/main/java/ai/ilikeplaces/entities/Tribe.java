package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.util.ExceptionCache;
import ai.ilikeplaces.util.jpa.Refresh;
import ai.ilikeplaces.util.jpa.RefreshException;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import ai.ilikeplaces.util.jpa.Refreshable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Always think of a {@link Tribe} as a VIEW on users. A way of looking at how user groups are handled.
 * <p/>
 * Album and wall are just additional attributes/features of a tribe.
 * <p/>
 * A Tribe is just a View. Making this otherwise makes interweaving with {@link PrivateLocation} and {@link PrivateEvent}
 * extremely difficult. This is why tribe's should not be assigned members with roles as Leader and Tribal.
 * <p/>
 * Given the fact that any tribal lady can conceive a child, enrolling a member to a {@link Tribe} can be a responsibility taken up by anyone.
 * <p/>
 * Removing members hence, should happen properly without leaving ACID issues.
 * <p/>
 * <p/>
 * So, when a person is removed from a tribe, he can still visit old private locations and events he was added since Tribe is just a VIEW.
 * VIEW now does not have the humanId, but is nevertheless present in the users data.
 * <p/>
 * <p/>
 * <p/>
 * <b>Tribes were introduces because of a few reasons:</b>
 * <p/>
 * Asking the user to just invite friends looked lame, old and boring
 * <p/>
 * People have clicks they hang around with
 * <p/>
 * People need a story/cause to act on as a group
 * <p/>
 * People need a clear connection between each other and the leaders to proceed
 * <p/>
 * People need a proper actionable path to proceed
 * <p/>
 * <p/>
 * <b>This class should facilitate mainly:</b>
 * <p/>
 * Adding/inviting friends through interacting with this entity (invitations will happen through this entity only in future)
 * <p/>
 * Interacting with friends of a tribe through its wall, included in this entity.
 * <p/>
 * This entity should know which places and moments it belongs to. This implies careful wiring. Adding a tribe to a moment should update this. Removing should update this. Same goes for places
 * <p/>
 * We will also provide an album to upload cause photos, and photo's that don't belong to any moment.
 * <p/>
 * A tribes leaders will be a place's and moment's leaders
 * <p/>
 * A tribes members will be a place's and moment's members
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
@Entity
public class Tribe implements Serializable, Refreshable<Tribe> {
// ------------------------------ FIELDS ------------------------------

    public Long tribeId;

    public String tribeName;
    public String tribeStory;

    public Wall tribeWall;

    public Album tribeAlbum;

    public Set<HumansTribe> tribeMembers;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Refreshable ---------------------

    @Override
    public Tribe refresh(RefreshSpec refreshSpec) throws RefreshException {
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }
}


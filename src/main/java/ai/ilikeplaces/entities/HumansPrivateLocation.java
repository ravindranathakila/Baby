package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.Return;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * This entity is NOT related to a humans public location bookings.
 * This comment was placed here to avoid logic confusion.
 * <p/>
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Dec 6, 2009
 * Time: 6:11:09 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
public class HumansPrivateLocation extends HumanEquals implements HumanPkJoinFace, HumansFriend, RefreshData<HumansPrivateLocation>, Serializable {

    public String humanId;

    public Human human;

    public List<PrivateLocation> privateLocationsViewed;

    public List<PrivateLocation> privateLocationsOwned;


    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @OneToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }

    public void setHuman(final Human human) {
        this.human = human;
    }

    @NOTE(note = "This implementation will be fast a.l.a the Human entity has lazy in its getters.")
    @Override
    @Transient
    public String getDisplayName() {
        return this.human.getHumansNet().getDisplayName();
    }

    @Override
    @Transient
    public boolean isFriend(final String friendsHumanId) {
        final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doDirtyIsHumansNetPeople(new ai.ilikeplaces.logic.validators.unit.HumanId(this.humanId), new ai.ilikeplaces.logic.validators.unit.HumanId(friendsHumanId));
        if (r.returnStatus() != 0) {
            throw new DBException(r.returnError());
        }
        return r.returnValue();
    }

    @Override
    public boolean notFriend(final String friendsHumanId) {
        return !isFriend(friendsHumanId);
    }

    @BIDIRECTIONAL
    @WARNING(warning = "Many",
             warnings = {"Not owner as deleting a location should automatically reflect in here, not vice versa.",
                     "DO NOT MAKE EAGER WHEN LOADING, WHICH CAUSES A GALACTIC FETCH ON ALMOST THE ENTIRE TABLE. MAKING LAZY MADE A HUGE PERFORMANCE IMPACT OF SCALE 10^2"})
    @NOTE(note = "Locations which this user is INVOLVED with, NOT specifically OWNS.")
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = PrivateLocation.privateLocationViewersCOL, fetch = FetchType.LAZY)
    public List<PrivateLocation> getPrivateLocationsViewed() {
        return privateLocationsViewed;
    }

    public void setPrivateLocationsViewed(final List<PrivateLocation> privateLocationsViewed) {
        this.privateLocationsViewed = privateLocationsViewed;
    }

    @BIDIRECTIONAL
    @WARNING(warning = "Many",
             warnings = {"Not owner as deleting a location should automatically reflect in here, not vice versa.",
                     "DO NOT MAKE EAGER WHEN LOADING, WHICH CAUSES A GALACTIC FETCH ON ALMOST THE ENTIRE TABLE. MAKING LAZY MADE A HUGE PERFORMANCE IMPACT OF SCALE 10^2"})
    @NOTE(note = "Locations which this user is INVOLVED with, NOT specifically OWNS.")
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = PrivateLocation.privateLocationOwnersCOL, fetch = FetchType.LAZY)
    public List<PrivateLocation> getPrivateLocationsOwned() {
        return privateLocationsOwned;
    }

    public void setPrivateLocationsOwned(List<PrivateLocation> privateLocationsOwned) {
        this.privateLocationsOwned = privateLocationsOwned;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() == o.getClass()) {
            final HumansPrivateLocation that = (HumansPrivateLocation) o;
            return (!(this.getHumanId() == null || that.getHumanId() == null)) && this.getHumanId().equals(that.getHumanId());
        } else {
            return matchHumanId(o);
        }
    }

    @Override
    public int hashCode() {
        return humanId != null ? humanId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HumansPrivateLocation{" +
                "human=" + human +
                '}';
    }


    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @throws ai.ilikeplaces.exception.DBFetchDataException
     *
     */
    @Override
    public HumansPrivateLocation refresh() throws DBFetchDataException {
        try {
            this.getPrivateLocationsOwned().size();
            this.getPrivateLocationsViewed().size();
        } catch (final Exception e) {
            throw new DBFetchDataException(e);
        }
        return this;
    }
}

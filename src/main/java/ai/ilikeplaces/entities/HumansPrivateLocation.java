package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.Return;

import javax.persistence.*;
import java.util.List;

/**
 * This entity is NOT related to a humans public location bookings.
 * This comment was placed here to avoid logic confusion.
 * <p/>
 * User: Ravindranath Akila
 * Date: Dec 6, 2009
 * Time: 6:11:09 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCycleListener.class)
public class HumansPrivateLocation extends HumanEquals implements HumanPkJoinFace, HumansFriend {

    private String humanId;

    private Human human;

    private List<PrivateLocation> privateLocationsViewed;

    private List<PrivateLocation> privateLocationsOwned;


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

    public void setHuman(final Human human) {
        this.human = human;
    }


    @BIDIRECTIONAL
    @WARNING(warning = "Not owner as deleting a location should automatically reflect in here, not vice versa.")
    @NOTE(note = "Locations which this user is INVOLVED with, NOT specifically OWNS.")
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = PrivateLocation.privateLocationViewersCOL, fetch = FetchType.EAGER)
    public List<PrivateLocation> getPrivateLocationsViewed() {
        return privateLocationsViewed;
    }

    public void setPrivateLocationsViewed(final List<PrivateLocation> privateLocationsViewed) {
        this.privateLocationsViewed = privateLocationsViewed;
    }

    @BIDIRECTIONAL
    @WARNING(warning = "Not owner as deleting a location should automatically reflect in here, not vice versa.")
    @NOTE(note = "Locations which this user is INVOLVED with, NOT specifically OWNS.")
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = PrivateLocation.privateLocationOwnersCOL, fetch = FetchType.EAGER)
    public List<PrivateLocation> getPrivateLocationsOwned() {
        return privateLocationsOwned;
    }

    public void setPrivateLocationsOwned(List<PrivateLocation> privateLocationsOwned) {
        this.privateLocationsOwned = privateLocationsOwned;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null) return false;

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


}

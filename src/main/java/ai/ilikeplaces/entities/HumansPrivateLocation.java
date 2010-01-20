package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.UNIDIRECTIONAL;
import ai.ilikeplaces.util.EntityLifeCyleListener;

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

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
@EntityListeners(EntityLifeCyleListener.class)
public class HumansPrivateLocation implements HumanPkJoinFace {

    private String humanId;

    private Human human;

    /**
     *
     */
    private List<PrivateLocation> privateLocations;


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


    @UNIDIRECTIONAL
    @NOTE(note = "Locations which this user is INVOLVED with, NOT specifically OWNS.")
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public List<PrivateLocation> getPrivateLocations() {
        return privateLocations;
    }

    public void setPrivateLocations(final List<PrivateLocation> privateLocations) {
        this.privateLocations = privateLocations;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final HumansPrivateLocation that = (HumansPrivateLocation) o;

        if (humanId != null ? !humanId.equals(that.humanId) : that.humanId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return humanId != null ? humanId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HumansPrivateLocation{" +
                "human=" + human +
                ", privateLocations=" + privateLocations +
                '}';
    }


}

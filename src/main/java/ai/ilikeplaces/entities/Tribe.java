package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.ExceptionCache;
import ai.ilikeplaces.util.jpa.RefreshException;
import ai.ilikeplaces.util.jpa.RefreshSpec;
import ai.ilikeplaces.util.jpa.Refreshable;

import javax.persistence.*;
import java.io.Serializable;
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
@Table(name = "Tribe", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class Tribe implements Serializable, Refreshable<Tribe>, RefreshData<Tribe>, Comparable<Tribe> {
// ------------------------------ FIELDS ------------------------------

    @Id
    @Column(name = "tribeId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long tribeId;

    @Column(name = "tribeName", nullable = false, length = 255)
    public String tribeName;

    @Column(name = "tribeStory", nullable = false, length = 1000)
    public String tribeStory;

    @_unidirectional
    @OneToOne(mappedBy = "wallId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Wall tribeWall;

    @_unidirectional
    @OneToOne(mappedBy = "albumId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Album tribeAlbum;

    @WARNING(warning = "Owner because once an tribe needs to be deleted, deleting this tribe is easier if owner." +
            "If this tribe is not the owner, individual owner viewer accepteee rejectee will have to delete their tribes individually.")
    @_bidirectional(ownerside = _bidirectional.OWNING.IS)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = HumansTribe.tribesCOL),
            inverseJoinColumns = @JoinColumn(name = tribeMembersCOL)
    )
    public Set<HumansTribe> tribeMembers;
    public static final String tribeMembersCOL = "tribeMembers";

// --------------------- GETTER / SETTER METHODS ---------------------

// ------------------------ ACCESSORS / MUTATORS ------------------------


    public Album getTribeAlbum() {
        return tribeAlbum;
    }

    public void setTribeAlbum(final Album tribeAlbum) {
        this.tribeAlbum = tribeAlbum;
    }

    public Long getTribeId() {
        return tribeId;
    }

    public void setTribeId(final Long tribeId) {
        this.tribeId = tribeId;
    }

    public Set<HumansTribe> getTribeMembers() {
        return tribeMembers;
    }

    public void setTribeMembers(final Set<HumansTribe> tribeMembers) {
        this.tribeMembers = tribeMembers;
    }

    public String getTribeName() {
        return tribeName;
    }

    public void setTribeName(String tribeName) {
        this.tribeName = tribeName;
    }

    public String getTribeStory() {
        return tribeStory;
    }

    public void setTribeStory(final String tribeStory) {
        this.tribeStory = tribeStory;
    }

    public Wall getTribeWall() {
        return tribeWall;
    }

    public void setTribeWall(final Wall tribeWall) {
        this.tribeWall = tribeWall;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "Tribe{" +
                "tribeId=" + tribeId +
                ", tribeName='" + tribeName + '\'' +
                '}';
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Comparable ---------------------


    @Override
    public int compareTo(final Tribe o) {
        return (int) (this.tribeId - o.tribeId);
    }

// --------------------- Interface RefreshData ---------------------

    /**
     * Calling this method will refresh any lazily fetched lists in this entity making them availabe for use.
     *
     * @return T
     * @throws ai.ilikeplaces.exception.DBFetchDataException
     *          in case the entity fails to refresh something inside it
     */
    @Override
    public Tribe refresh() throws DBFetchDataException {
        this.getTribeMembers().size();
        return this;
    }

// --------------------- Interface Refreshable ---------------------

    @Override
    public Tribe refresh(RefreshSpec refreshSpec) throws RefreshException {
        throw ExceptionCache.METHOD_NOT_IMPLEMENTED;
    }

// -------------------------- OTHER METHODS --------------------------

    @Transient
    public Tribe setTribeAlbumR(final Album tribeAlbum) {
        this.tribeAlbum = tribeAlbum;
        return this;
    }

    @Transient
    public Tribe setTribeNameR(String tribeName) {
        this.tribeName = tribeName;
        return this;
    }

    @Transient
    public Tribe setTribeStoryR(final String tribeStory) {
        this.tribeStory = tribeStory;
        return this;
    }

    @Transient
    public Tribe setTribeWallR(final Wall tribeWall) {
        this.tribeWall = tribeWall;
        return this;
    }
}


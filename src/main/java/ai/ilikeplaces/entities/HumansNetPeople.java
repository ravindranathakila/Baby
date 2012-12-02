package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc._unidirectional;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.util.Return;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * These are the people needed for an event
 * <p/>
 * <p/>
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Dec 8, 2009
 * Time: 9:54:03 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "HumansNetPeople", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@NamedQueries(
        {
                @NamedQuery(
                        name = "FindHumansNetPeoplesWhoHaveMeAsAFriend",
                        query = "SELECT humansNetPeople FROM HumansNetPeople humansNetPeople " +
                                "WHERE humansNetPeople.humanId " +
                                "IN(" +
                                "SELECT humansNetPeople2 FROM HumansNetPeople humansNetPeople2, HumansNetPeople humansNetPeople3 " +
                                "WHERE humansNetPeople3.humanId = :humanId)"//Don't ask me how I did it. It took me hours! Weather wasn't ideal either
                )
        }
)
@EntityListeners({EntityLifeCycleListener.class})
public class HumansNetPeople extends HumanEquals implements HumansFriend, HumanIdFace, Serializable {
// ------------------------------ FIELDS ------------------------------

    public final static String humanIdCOL = "humanId";

    public final static String FindHumansNetPeoplesWhoHaveMeAsAFriend = "FindHumansNetPeoplesWhoHaveMeAsAFriend";
    private static final String HUMANS_NET_PEOPLE = "HumansNetPeople{";
    private static final String HUMAN_ID = "humanId='";
    private static final char CHAR = '}';
    private static final char BACKSLASH = '\'';

    @Id
    @Column(name = "humanId")
    public String humanId;


    @OneToOne(mappedBy = "humanId", cascade = CascadeType.REFRESH)
    //@PrimaryKeyJoinColumn
    public HumansNet humansNet;

    @NOTE(note = "MANY IS THE OWNING SIDE, HENCE REFRESH. SINCE THIS IS SELF REFERENTIAL, A REFRESH WITH SELF SHOULD NOT HAPPEN.")
    @_unidirectional(note = "Asymmetric Relationship")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = humanIdCOL)
    public List<HumansNetPeople> humansNetPeoples;

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    public HumansNet getHumansNet() {
        return humansNet;
    }

    public void setHumansNet(HumansNet humansNet) {
        this.humansNet = humansNet;
    }

    public List<HumansNetPeople> getHumansNetPeoples() {
        return humansNetPeoples;
    }

    public void setHumansNetPeoples(final List<HumansNetPeople> humansNetPeoples) {
        this.humansNetPeoples = humansNetPeoples;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (getClass() == o.getClass()) {
            final HumansNetPeople that = (HumansNetPeople) o;
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
        return HUMANS_NET_PEOPLE +
                HUMAN_ID + humanId + BACKSLASH +
                CHAR;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface HumansFriend ---------------------

    @NOTE(note = "This implementation will be fast a.l.a the Human entity has lazy in its getters.")
    @Override
    @Transient
    public String getDisplayName() {
        return this.getHumansNet().getDisplayName();
    }

    @Override
    @Transient
    public boolean ifFriend(final String friendsHumanId) {
        final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doDirtyIsHumansNetPeople(new ai.ilikeplaces.logic.validators.unit.HumanId(this.humanId), new ai.ilikeplaces.logic.validators.unit.HumanId(friendsHumanId));
        if (r.returnStatus() != 0) {
            throw new DBException(r.returnError());
        }
        return r.returnValue();
    }

    @Override
    public boolean notFriend(final String friendsHumanId) {
        return !ifFriend(friendsHumanId);
    }
}

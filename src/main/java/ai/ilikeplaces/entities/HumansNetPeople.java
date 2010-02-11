package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.UNIDIRECTIONAL;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.util.Return;

import javax.persistence.*;
import java.util.List;

/**
 * These are the people needed for an event
 * <p/>
 * <p/>
 * User: Ravindranath Akila
 * Date: Dec 8, 2009
 * Time: 9:54:03 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Entity
public class HumansNetPeople extends HumanEquals implements HumansFriend {
    private String humanId;
    private HumansNet humansNet;
    private List<HumansNetPeople> humansNetPeoples;

    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @Transient
    @Override
    public Human getHuman() {
        return getHumansNet().getHuman();
    }

    @OneToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    public HumansNet getHumansNet() {
        return humansNet;
    }

    public void setHumansNet(HumansNet humansNet) {
        this.humansNet = humansNet;
    }

    @NOTE(note = "This implementation will be fast a.l.a the Human entity has lazy in its getters.")
    @Override
    @Transient
    public String getDisplayName() {
        return this.getHumansNet().getDisplayName();
    }

    @Override
    @Transient
    public boolean isFriend(final String friendsHumanId) {
        final Return<Boolean> r = DB.getHumanCRUDHumanLocal(true).doDirtyIsHumansNetPeople(new HumanId(this.humanId), new HumanId(friendsHumanId));
        if (r.returnStatus() != 0) {
            throw new DBException(r.returnError());
        }
        return r.returnValue();
    }

    @NOTE(note = "MANY IS THE OWNING SIDE, HENCE REFRESH. SINCE THIS IS SELF REFERENTIAL, A REFRESH WITH SELF SHOULD NOT HAPPEN.")
    @UNIDIRECTIONAL(note = "Asymmetric Relationship")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    public List<HumansNetPeople> getHumansNetPeoples() {
        return humansNetPeoples;
    }

    public void setHumansNetPeoples(final List<HumansNetPeople> humansNetPeoples) {
        this.humansNetPeoples = humansNetPeoples;
    }

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
}

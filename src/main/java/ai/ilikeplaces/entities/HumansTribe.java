package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.BIDIRECTIONAL;
import ai.ilikeplaces.doc.UNIDIRECTIONAL;
import ai.ilikeplaces.doc.WARNING;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/12/11
 * Time: 7:47 PM
 */
@WARNING("THIS ENTITY IS NOT GUARANTEED TO 'BE' EVEN THOUGH A HUMAN IS SIGNED UP. SO CREATE IT IF NOT PRESENT!")
@Entity
public class HumansTribe implements HumansFriend{
    public String humanId;

    public Set<Tribe> tribes;

    @Id
    public String getHumanId() {
        return humanId;
    }

    @Override
    public Human getHuman() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDisplayName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isFriend(String friendsHumanId) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean notFriend(String friendsHumanId) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setHumanId(final String humanId) {
        this.humanId = humanId;
    }

    @Transient
    public HumansTribe setHumanIdR(final String humanId) {
        this.humanId = humanId;
        return this;
    }

    @BIDIRECTIONAL(ownerside = BIDIRECTIONAL.OWNING.NOT)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public Set<Tribe> getTribes() {
        return tribes;
    }

    public void setTribes(final Set<Tribe> tribes) {
        this.tribes = tribes;
    }
}

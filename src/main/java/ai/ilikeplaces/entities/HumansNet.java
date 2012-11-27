package ai.ilikeplaces.entities;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.exception.DBException;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.util.EntityLifeCycleListener;
import ai.ilikeplaces.util.Return;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This entity would be about friends. <p/>
 * <p/>
 * THEORY:<p/>
 * <p/>
 * SIMPLE VS COMPLEX RELATIONSHIP MANAGEMENT
 * Twitter is still dominant. Simplicity plays a huge role.
 * Just as similar to a Wizard setup as opposed to complex control panel setup preferred,
 * the ideal feature would be to offer complex relationships presented with simplicity.
 * Hence here we predefine "Knowns, Family, Relatives, Best Friends, Friends, Professionals, Crosses, Rejects, Followers"
 * and facilitate 3 other customs.
 * Rejects cannot see this person.
 * Followers, frankly is like twitter. People who like you and you can't give a shit about as the base might be huge.
 * While some of these are exclusive, some intersect, and some are subsets. Thia has to be fixed.
 * <p/>
 * RESEARCH: <p/>
 * TYPES OF RELATIONSHIPS IN SOCIAL NETWORKS <p/>
 * To be honest I thought of the relationship mode of asymmetry myself.
 * That is like in real world. If you are decide a person is a friend, you just do it.
 * There is no need to announce it to the friend. The friend on the other hand, just might
 * think of you as a "known" person. Each other only know by themselves who the other person is.
 * This is the model ilikeplaces uses.
 * <p/>
 * http://bokardo.com/archives/relationship-symmetry-in-social-networks-why-facebook-will-go-fully-asymmetric/
 * <p/>
 * <p/>
 * <p/>
 * FIXING PRIVACY ISSUE:<p/>
 * It wasn't easy to find out a easy mid point between privacy levels, relationship categories and update feeds. Then
 * again there are followers to whom a user has to cater. The best approach I think is introducing a "Privacy Level".
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * PENDING RESEARCH: <p/>
 * <p/>
 * HOW TO SOLVE THE LOST FRIENDS PARADOX tm<p/>
 * Well we all know, if we are out of our social networks for sometime, others just forget us.
 * This is related to how updates are being shared. I think ilikeplaces should introduce a
 * "Miss" concept or maybe for guys rather "Where t f are you?" type of thing(Let the user define the sentence?).
 * I don't know if these ideas could be stolen, but at least since they cannot be patented yet by me,
 * we can beat them to it.          <p/>
 * <p/>
 * HOW TO SOLVE LOVE WITHDRAWAL<P/>
 * <p/>
 * Married couples and love affairs tend to make networkers withdraw from a lot of activity.
 * http://family.jrank.org/pages/1602/Social-Networks-Social-Network-Structure-Relationship-Opportunities-Constraints.html
 * <p/>
 * HOW TO SOLVE GENERIC NETWORK ISSUE<p/>
 * <p/>
 * Having a social network account needs to be "real-time" and "painting". The network should be able to outline and
 * help grow an individuals future. Like, make new connections etc. This probably needs more agendas than just this
 * entity.
 * <p/>
 * Well there should be a name to it. Not "What are you up to". Not "Updates". "Has/Has Just | Is | Will, Will be"
 * <p/>
 * But then again updates alone will not help. In fact, it might suck since who cares what we are up to THAT much. This
 * certainly needs revision.                                     <p/>
 * <p/>
 * http://gigaom.com/2009/04/08/facebook-population-200m-faces-an-identity-crisis/ <p/>
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * user: Ravindranath Akila
 * Date: Dec 6, 2009
 * Time: 6:09:59 PM
 *
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Table(name = "HumansNet", schema = "KunderaKeyspace@ilpMainSchema")
@Entity
@EntityListeners({EntityLifeCycleListener.class})
public class HumansNet implements HumanPkJoinFace, HumansFriend, Serializable {

    @Id
    @Column(name = "humanId")
    public String humanId;

    @OneToOne(mappedBy = "humanId", cascade = CascadeType.REFRESH)
    //@PrimaryKeyJoinColumn
    public Human human;

    @NOTE(note = "Display name is used for adding removing users etc. This can also be the nick name." +
            "The compulsory requirement for this name is that others know the user by this name." +
            "This name is important for us as it helps DB performance(instead of loading identity bean." +
            "This is one place where the benefit breaking table with PK is elaborated.(we made this displayName entry much " +
            "later in the development cycle)")
    @Column(name = "displayName")
    public String displayName;

    @WARNING(warning = "Do not change LAZY loading.",
            warnings = {"If you are changing, check with Human which fetches this eager.",
                    "If you are changing, check how to efficiently fetch displayName.",
                    "You could use pkjoincolumn between HumansNet and the requiring entity."})
    @OneToOne(mappedBy = "humanId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@PrimaryKeyJoinColumn
    public HumansNetPeople humansNetPeople;

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }


    public Human getHuman() {
        return human;
    }

    @Override
    public void setHuman(final Human human) {
        this.human = human;
    }

    public String getDisplayName() {
        return displayName;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public HumansNetPeople getHumansNetPeople() {
        return humansNetPeople;
    }

    public void setHumansNetPeople(final HumansNetPeople humansNetPeople) {
        this.humansNetPeople = humansNetPeople;
    }
}

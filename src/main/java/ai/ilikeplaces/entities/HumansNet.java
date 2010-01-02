package ai.ilikeplaces.entities;

import javax.persistence.*;
import java.util.List;

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
 *  <p/>
 * RESEARCH: <p/>
 * TYPES OF RELATIONSHIPS IN SOCIAL NETWORKS <p/>
 * To be honest I thought of the relationship mode of asymmetry myself.
 * That is like in real world. If you are decide a person is a friend, you just do it.
 * There is no need to announce it to the friend. The friend on the other hand, just might
 * think of you as a "known" person. Each other only know by themselves who the other person is.
 * This is the model ilikeplaces uses.
 * <p/>
 * http://bokardo.com/archives/relationship-symmetry-in-social-networks-why-facebook-will-go-fully-asymmetric/
 *
 * <P/>
 *
 * FIXING PRIVACY ISSUE:<p/>
 * It wasn't easy to find out a easy mid point between privacy levels, relationship categories and update feeds. Then
 * again there are followers to whom a user has to cater. The best approach I think is introducing a "Privacy Level".
 *
 * 
 * <p/>
 * <p/>
 *
 * PENDING RESEARCH: <p/>
 *
 * HOW TO SOLVE THE LOST FRIENDS PARADOX tm<p/>
 * Well we all know, if we are out of our social networks for sometime, others just forget us.
 * This is related to how updates are being shared. I think ilikeplaces should introduce a
 * "Miss" concept or maybe for guys rather "Where t f are you?" type of thing(Let the user define the sentence?).
 * I don't know if these ideas could be stolen, but at least since they cannot be patented yet by me,
 * we can beat them to it.          <p/>
 *
 * HOW TO SOLVE LOVE WITHDRAWAL<P/>
 *
 * Married couples and love affairs tend to make networkers withdraw from a lot of activity.
 * http://family.jrank.org/pages/1602/Social-Networks-Social-Network-Structure-Relationship-Opportunities-Constraints.html
 *
 * HOW TO SOLVE GENERIC NETWORK ISSUE<p/>
 *
 * Having a social network account needs to be "real-time" and "painting". The network should be able to outline and
 * help grow an individuals future. Like, make new connections etc. This probably needs more agendas than just this
 * entity.
 *
 * Well there should be a name to it. Not "What are you up to". Not "Updates". "Has/Has Just | Is | Will, Will be"
 *
 * But then again updates alone will not help. In fact, it might suck since who cares what we are up to THAT much. This
 * certainly needs revision.                                     <p/>
 * 
 * http://gigaom.com/2009/04/08/facebook-population-200m-faces-an-identity-crisis/ <p/>
 *
 *
 * <p/>
 * <p/>
 * user: Ravindranath Akila
 * Date: Dec 6, 2009
 * Time: 6:09:59 PM
 *
 * @author Ravindranath Akila
 */
@Entity
public class HumansNet {

    private String humanId;
    private Human human;
    private HumansNetPeople humansNetPeople;



    @Id
    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(final String humanId__) {
        this.humanId = humanId__;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Human getHuman() {
        return human;
    }
}

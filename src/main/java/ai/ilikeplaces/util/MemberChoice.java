package ai.ilikeplaces.util;

import ai.ilikeplaces.doc.License;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Feb 7, 2010
 * Time: 12:30:41 PM
 */

/**
 * A class to help adding removing members from a list on enroll basis.
 * For example, if we need to track which members are added for an event, this can be used.
 *
 * The user interface is provided with this member, along with the add and remove lists.
 *
 * When a user selects/unselscts(radio button say) the member gets added to the relevant list.
 *
 * 
 * @param <L>
 * @param <M>
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class MemberChoice<L extends Set<M>, M> {
    private L added;
    private L removed;

    private M member;

    /**
     * 
     * @param member  reference to the member to be added or removed
     * @param added   reference to the list to add members
     * @param removed reference to the list to remove members
     */
    public MemberChoice(final M member, final L added, final L removed) {
        this.member = member;
        
        this.added = added;
        this.removed = removed;
    }

    public boolean add() {
        removed.remove(member);
        return added.add(member);
    }

    public boolean remove() {
        added.remove(member);
        return removed.add(member);
    }
}

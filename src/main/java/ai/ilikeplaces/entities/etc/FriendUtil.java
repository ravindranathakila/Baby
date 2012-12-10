package ai.ilikeplaces.entities.etc;


import ai.util.HumanId;
import ai.util.Return;

/**
 * Somebody has to register the checking mechanism here. If not each check will throw an exception.
 * This was done to separate this module from any other module in the project.
 * <p/>
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 2/12/12
 * Time: 4:44 PM
 */
public class FriendUtil {

    public interface CheckHumanApproach {
        Return<Boolean> check(final HumanId me, final HumanId other);
    }

    public static CheckHumanApproach APPROACH_FOR_CHECKING_HUMANS_FRIEND = null;

    public static Return<Boolean> check(final HumanId me, final HumanId other) {
        return APPROACH_FOR_CHECKING_HUMANS_FRIEND.check(me, other);
    }
}

package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.etc.HumanId;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 3/23/12
 * Time: 9:55 PM
 */
public interface AdaptableSignupCallback {

    public String afterInvite(final HumanId invitee);

    public String jsToSend(final HumanId invitee);
}

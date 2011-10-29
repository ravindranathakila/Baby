package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/26/11
 * Time: 8:40 PM
 */
public class ListenerTribes implements ItsNatServletRequestListener {
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse itsNatServletResponse) {
        new AbstractSkeletonListener(request__) {
            @Override
            protected void init(ItsNatHTMLDocument itsNatHTMLDocument__, HTMLDocument hTMLDocument__, ItsNatDocument itsNatDocument__, Object... initArgs) {

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        setLoginWidget(request__);
                    }

                    setTitle:
                    {
                        //set below
                    }

                    setProfileLink:
                    {
                        setProfileLink();
                    }
                    setProfileDataLink:
                    {
                        setProfileDataLink();
                    }
                    sideBarFriends:
                    {
                        setSideBarFriends(request__, DownTownFlowCriteria.DownTownFlowDisplayComponent.TRIBES);
                    }
                    signinupActionNotice:
                    {
                        //We do nothing now
                    }
                    SetNotifications:
                    {
                        setNotifications();
                    }
                }

                if (getUsername() != null) {

                    new TribeWidget(request__, new TribeWidgetCriteria(), $(Controller.Page.Skeleton_center_content));
                    new TribeCreateWidget(request__, new TribeCreateWidgetCriteria(new HumanId(getUsernameAsValid())), $(Controller.Page.Skeleton_center_content));

                } else {
                    //WE COULD STILL GIVE A TRIBE CREATION WIDGET WITH USERS EMAIL AND FRIENDS EMAILS. ONCE USER SIGNS UP, THE TRIBE CAN BE CREATED.
                }
            }
        };
    }
}

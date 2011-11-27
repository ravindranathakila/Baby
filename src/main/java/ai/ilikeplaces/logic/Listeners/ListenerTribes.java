package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.Tribe;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.logic.Listeners.widgets.teach.TeachTribe;
import ai.ilikeplaces.logic.Listeners.widgets.teach.TeachTribeCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.VLong;
import ai.ilikeplaces.servlets.Controller;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.HashSet;
import java.util.Set;

import static ai.ilikeplaces.util.Loggers.USER_EXCEPTION;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 10/26/11
 * Time: 8:40 PM
 */
public class ListenerTribes implements ItsNatServletRequestListener {
    private static final String COMMA = ",";
    // ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ItsNatServletRequestListener ---------------------

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


                    /*Be fault tolerant with the user. Just parse category here. Later parse other values when needed*/
                    int mode = -1;
                    try {
                        final String modeString = request__.getServletRequest().getParameter(Controller.Page.DocTribesMode);
                        mode = modeString != null ? Integer.parseInt(modeString) : Controller.Page.DocTribesModeCreate;
                    } catch (final NumberFormatException nfe_) {
                        USER_EXCEPTION.error("", nfe_);
                    }
                    switch (mode) {
                        case Controller.Page.DocTribesModeCreate: {
                            showTribeCreateWidget(request__, getUsernameAsValid(), $(Controller.Page.Skeleton_center_skeleton));
                            break;
                        }


                        case Controller.Page.DocTribesModeView: {

                            try {
                                final String whichString = request__.getServletRequest().getParameter(Controller.Page.DocTribesWhich);
                                if (whichString != null) {
                                    final String[] whichStringArr = whichString.split(COMMA);
                                    final Set<Long> which = new HashSet<Long>(whichStringArr.length);

                                    for (int i = 0, whichStringArrLength = whichStringArr.length; i < whichStringArrLength; i++) {
                                        try {
                                            which.add(Long.parseLong(whichStringArr[i].trim()));
                                        } catch (NumberFormatException e) {
                                            //Ignore this pathetic situation and add 0 which will never match with a tribe
                                            which.add(0L);
                                        }
                                    }

                                    final Set<Tribe> humansTribes;

                                    FirstWeGetThisPersonsTribes_ThisIsImportant:
                                    {
                                        humansTribes = DB.getHumanCRUDTribeLocal(false).getHumansTribes(new HumanId(getUsernameAsValid()));
                                    }

                                    for (final Tribe tribe : humansTribes) {
                                        if (which.contains(tribe.getTribeId())) {
                                            new TribeWidget(
                                                    request__,
                                                    new TribeWidgetCriteria()
                                                            .setHumanId(geHumanIdAsValid())
                                                            .setTribeId(new VLong().setObjAsValid(tribe.getTribeId())),
                                                    $(Controller.Page.Skeleton_center_skeleton));
                                        }
                                    }

                                } else {
                                    //We show the tribe create widget
                                    showTribeCreateWidget(request__, getUsernameAsValid(), $(Controller.Page.Skeleton_center_skeleton));
                                }

                            } catch (final NumberFormatException nfe_) {
                                USER_EXCEPTION.error("", nfe_);
                            }


                            break;
                        }

                        default: {
                            showTribeCreateWidget(request__, getUsernameAsValid(), $(Controller.Page.Skeleton_center_skeleton));
                            break;
                        }
                    }
                } else {
                    //WE COULD STILL GIVE A TRIBE CREATION WIDGET WITH USERS EMAIL AND FRIENDS EMAILS. ONCE USER SIGNS UP, THE TRIBE CAN BE CREATED.

                    new TeachTribe(request__, new TeachTribeCriteria(null), $(Controller.Page.Skeleton_center_skeleton));
                }
            }
        };
    }

    private void showTribeCreateWidget(ItsNatServletRequest request__, final String humanId, final Element appendToElement) {
        new TribeCreateWidget(
                request__,
                new TribeCreateWidgetCriteria(
                        new HumanId(humanId)),
                appendToElement);
    }
}

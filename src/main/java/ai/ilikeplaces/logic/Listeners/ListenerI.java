package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.logic.Listeners.widgets.FriendAdd;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.Listeners.widgets.WallWidgetHumansWall;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.ElementComposer;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ai.ilikeplaces.servlets.Controller.Page.*;
import static ai.ilikeplaces.util.Loggers.EXCEPTION;

/**
 * @author Ravindranath Akila
 */
@WARNING(warning = "Remember, this shows profiles of other users to the current user. Might impose serious privacy issues if " +
        "not handled with utmost care")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerI implements ItsNatServletRequestListener {
    public static final String USER_PROFILE = "up";
    private static final String NOT_YET_YOUR_FRIEND = "Not yet your friend";
    private static final String VIEW_FRIEND_SUCCESSFUL = "View Friend Successful.";
    private static final String NOT_FRIEND = "Not Friend.";
    private static final String SORRY_I_ENCOUNTERED_AN_ERROR_IN_REDIRECTING_THE_USER_FROM_I_PAGE = "SORRY! I ENCOUNTERED AN ERROR IN REDIRECTING THE USER FROM 'I' PAGE {}";

    /**
     * @param request__
     * @param response__
     */
    @WARNING(warning = "Remember, this shows profiles of other users to the current user. Might impose serious privacy issues if " +
            "not handled with utmost care")
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractSkeletonListener(request__) {

            private void redirectToSomeOtherPage(final ItsNatServletResponse response) {
                try {
                    ((HttpServletResponse) response.getServletResponse()).sendRedirect(Controller.Page.Organize.getURL());
                } catch (final IOException e) {
                    Loggers.EXCEPTION.error(SORRY_I_ENCOUNTERED_AN_ERROR_IN_REDIRECTING_THE_USER_FROM_I_PAGE, e);
                }
            }

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

                sl.appendToLogMSG("Returning I Page");


                final String requestedProfile = DB.getHumanCRUDHumanLocal(true).doDirtyProfileFromURL(request__.getServletRequest().getParameter(USER_PROFILE)).returnValueBadly();


                sl.appendToLogMSG("Requested Profile:" + requestedProfile);

                if (getUsername() == null) {
                    sl.complete(Loggers.LEVEL.DEBUG, "No Login." + Loggers.DONE);
                    redirectToSomeOtherPage(response__);
                } else {//User is logged on, now other things
                    if (requestedProfile == null) {//This user isn't alive
                        sl.complete(Loggers.LEVEL.DEBUG, "No Such Live User." + Loggers.DONE);
                        redirectToSomeOtherPage(response__);
                    } else {//This user should be a friend
                        //Be careful who checks who here. this user should have been added by the profile we are visiting as friend.(asymetric friend addition)
                        if (DB.getHumanCRUDHumanLocal(true).doNTxIsHumansNetPeople(new HumanId(requestedProfile), new HumanId(getUsernameAsValid())).returnValue()
                                || getUsernameAsValid().equals(requestedProfile)) {

                            layoutNeededForAllPages:
                            {
                                setLoginWidget:
                                {
                                    setLoginWidget((ItsNatServletRequest) initArgs[0]);
                                }

                                setTitle:
                                {
                                    setTitle((ItsNatServletRequest) initArgs[0]);
                                }

                                signOnDisplayLink:
                                {
                                    $(Skeleton_othersidebar_identity).setTextContent(DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(requestedProfile).getDisplayName());
                                }
                                setProfileLink:
                                {
                                    setProfileDataLink();
                                }
                                setProfilePhotoLink:
                                {
                                    try {
                                        if (getUsername() != null) {
                                            /**
                                             * TODO check for db failure
                                             */
                                            final String url = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansProfilePhoto(new HumanId(requestedProfile)).returnValueBadly();
                                            $(Skeleton_profile_photo).setAttribute(MarkupTag.IMG.src(),
                                                    url == null || url.isEmpty() ?
                                                            RBGet.getGlobalConfigKey("PROFILE_PHOTO_DEFAULT") :
                                                            RBGet.getGlobalConfigKey("PROFILE_PHOTOS") + url);
                                        }
                                    } catch (final Throwable t) {
                                        EXCEPTION.error("{}", t);

                                    }
                                }
                                setSidebarFriends:
                                {
                                    setSideBarFriends((ItsNatServletRequest) initArgs[0]);
                                }

                                setAddAsFriendIfNotFriend:
                                {
                                    try {
                                        final Human me = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(getUsernameAsValid());
                                        if (/*Self*/!me.getHumanId().equals(requestedProfile)
                                                /*Other*/ && me.notFriend(requestedProfile)) {
                                            new UserProperty(request__, $(Skeleton_center_content), new HumanId(requestedProfile)) {
                                                protected void init(final Object... initArgs) {
                                                    $$(Controller.Page.user_property_content).appendChild(
                                                            ElementComposer.compose($$(MarkupTag.DIV))
                                                                    .$ElementSetText(NOT_YET_YOUR_FRIEND).get());
                                                    new FriendAdd(request__, $$(Controller.Page.user_property_content), new HumanId(requestedProfile).getSelfAsValid(), new HumanId(getUsernameAsValid()).getSelfAsValid()) {
                                                    };
                                                }
                                            };
                                        }
                                    } catch (final Throwable t) {
                                        EXCEPTION.error("{}", t);

                                    }
                                }
                                setWall:
                                {
                                    try {
                                        new WallWidgetHumansWall(request__, $(Skeleton_center_content), new HumanId(requestedProfile), new HumanId(getUsernameAsValid()));
                                    } catch (final Throwable t) {
                                        EXCEPTION.error("{}", t);

                                    }
                                }
                            }
                            sl.complete(Loggers.LEVEL.DEBUG, VIEW_FRIEND_SUCCESSFUL + Loggers.DONE);
                        } else {
                            sl.complete(Loggers.LEVEL.DEBUG, NOT_FRIEND + Loggers.DONE);
                            redirectToSomeOtherPage(response__);
                        }
                    }
                }

            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(
                    final ItsNatHTMLDocument itsNatHTMLDocument__,
                    final HTMLDocument hTMLDocument__,
                    final ItsNatDocument itsNatDocument__) {
            }
        };
    }
}
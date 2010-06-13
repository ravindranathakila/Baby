package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractListener;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.SmartLogger;
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

    /**
     * @param request__
     * @param response__
     */
    @WARNING(warning = "Remember, this shows profiles of other users to the current user. Might impose serious privacy issues if " +
            "not handled with utmost care")
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__) {

            private void redirectToSomeOtherPage(final ItsNatServletResponse response) {
                try {
                    ((HttpServletResponse) response.getServletResponse()).sendRedirect(Controller.Page.Organize.getURL());
                } catch (final IOException e) {
                    Loggers.EXCEPTION.error("SORRY! I ENCOUNTERED AN ERROR IN REDIRECTING THE USER FROM 'I' PAGE {}", e);
                }
            }

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

                final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS,"Returning I Page",60000,null,true);


                final String requestedProfile = DB.getHumanCRUDHumanLocal(true).doDirtyProfileFromURL(request__.getServletRequest().getParameter(USER_PROFILE)).returnValueBadly();


                sl.appendToLogMSG("Requested Profile:" + requestedProfile);

                if (getUsername() == null) {
                    sl.complete(Loggers.LEVEL.DEBUG,"No Login."+Loggers.DONE);
                    redirectToSomeOtherPage(response__);
                } else {//User is logged on, now other things
                    if (requestedProfile == null) {//This user isn't alive
                        sl.complete(Loggers.LEVEL.DEBUG,"No Such Live User."+Loggers.DONE);
                        redirectToSomeOtherPage(response__);
                    } else {//This user should be a friend
                        //Be careful who checks who here. this user should have been added by the profile we are visiting as friend.(asymetric friend addition)
                        if (DB.getHumanCRUDHumanLocal(true).doNTxIsHumansNetPeople(new HumanId(requestedProfile), new HumanId(getUsernameAsValid())).returnValue()) {

                            layoutNeededForAllPages:
                            {
                                setLoginWidget:
                                {
                                    try {
                                        new SignInOn(itsNatDocument__, $(Skeleton_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                                        };
                                    } catch (final Throwable t) {
                                        Loggers.EXCEPTION.error("{}", t);
                                    }
                                }

                                signOnDisplayLink:
                                {
                                    $(Skeleton_othersidebar_identity).setTextContent(DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(requestedProfile).getDisplayName());
                                }
                                setProfileLink:
                                {
                                    try {
                                        if (getUsername() != null) {
                                            $(Skeleton_othersidebar_profile_link).setAttribute("href", Controller.Page.Profile.getURL());
                                        } else {
                                            $(Skeleton_othersidebar_profile_link).setAttribute("href", Controller.Page.signup.getURL());
                                        }
                                    } catch (final Throwable t) {
                                        Loggers.EXCEPTION.error("{}", t);

                                    }
                                }
                                setProfilePhotoLink:
                                {
                                    try {
                                        if (getUsername() != null) {
                                            /**
                                             * TODO check for db failure
                                             */
                                            String url = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansProfilePhoto(new HumanId(requestedProfile)).returnValueBadly();
                                            url = url == null ? null : RBGet.globalConfig.getString("PROFILE_PHOTOS") + url;
                                            if (url != null) {
                                                $(Skeleton_profile_photo).setAttribute(MarkupTag.IMG.src(), url);
                                            }
                                        }
                                    } catch (final Throwable t) {
                                        EXCEPTION.error("{}", t);

                                    }
                                }

                                setWall:
                                {
                                    //@TODO
                                }
                            }
                            sl.complete(Loggers.LEVEL.DEBUG,"View Friend Successful."+Loggers.DONE);
                        } else {
                            sl.complete(Loggers.LEVEL.DEBUG,"Not Friend."+Loggers.DONE);
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
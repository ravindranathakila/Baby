package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.logic.Listeners.widgets.DisplayName;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;
import static ai.ilikeplaces.util.Loggers.EXCEPTION;
import static ai.ilikeplaces.util.Loggers.LEVEL;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jul 12, 2010
 * Time: 9:54:25 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class AbstractSkeletonListener extends AbstractListener {

    public static final String DISCOVER_AND_HAVE_FUN_IN_PLACES = "Discover and Have Fun in Places! ";
    private static final String NO_LOGIN = "NoLogin";
    private static final String BN = "bn";
    private static final String PROFILE_PHOTOS = "PROFILE_PHOTOS";
    private static final String FABRICATING_SKELETON_PAGE = "Fabricating skeleton page";
    private static final String AI_ILIKEPLACES_LOGIC_LISTENERS_LISTENER_MAIN_0004 = "ai.ilikeplaces.logic.Listeners.ListenerMain.0004";

    /**
     * @param request_
     */
    public AbstractSkeletonListener(final ItsNatServletRequest request_) {
        super(request_, request_);
    }

    /**
     * Initializes your document here by appending fragments.
     * <p/>
     * If/when you are overriding this method, remember to call super.init(with required params)  so that,
     * the template is initialized.
     *
     * @param itsNatHTMLDocument__
     * @param hTMLDocument__
     * @param itsNatDocument__
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
        final ItsNatServletRequest request__ = (ItsNatServletRequest) initArgs[0];

        itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

        final ResourceBundle gUI = RBGet.gui();

        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.SERVER_STATUS, FABRICATING_SKELETON_PAGE, 100, null, true);

        layoutNeededForAllPages:
        {
            setLoginWidget:
            {
                try {
                    new SignInOn(request__, $(Skeleton_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                    };
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);
                }
            }
            setTitle:
            {
                try {
                    setMainTitle:
                    {
                        $(skeletonTitle).setTextContent(
                                DISCOVER_AND_HAVE_FUN_IN_PLACES +
                                        RBGet.globalConfig.getString(BN));

                    }
                    setMetaDescription:
                    {
                        $(skeletonTitle).setAttribute(MarkupTag.META.namee(),
                                                      DISCOVER_AND_HAVE_FUN_IN_PLACES +
                                                              RBGet.globalConfig.getString(BN));
                    }
                }
                catch (final Throwable t) {
                    Loggers.DEBUG.debug(t.getMessage());
                }
            }
            signOnDisplayLink:
            {
                try {
                    if (getUsername() != null) {
                        final Element usersName = $(MarkupTag.P);
                        usersName.setTextContent(gUI.getString(AI_ILIKEPLACES_LOGIC_LISTENERS_LISTENER_MAIN_0004) + getUsernameAsValid());
                        //$(Skeleton_othersidebar_identity).appendChild(usersName);
                        new DisplayName(request__, $(Skeleton_othersidebar_identity), new HumanId(getUsernameAsValid()), request__.getServletRequest()) {
                        };
                    } else {
                        final Element locationElem = $(MarkupTag.P);
                        locationElem.setTextContent(gUI.getString(NO_LOGIN));
                        $(Skeleton_othersidebar_identity).appendChild(locationElem);
                    }
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);
                }

            }
            setProfileLink:
            {
                try {
                    if (getUsername() != null) {
                        $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.Profile.getURL());
                    } else {
                        $(Skeleton_othersidebar_profile_link).setAttribute(MarkupTag.A.href(), Controller.Page.signup.getURL());
                    }
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);

                }
            }
            setProfileDataLink:
            {
                try {
                    if (getUsername() != null) {
                        final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(new HumanId(getUsername()).getSelfAsValid());
                        if (r.returnStatus() == 0) {
                            final HumansIdentity hi = r.returnValue();
                            $(Skeleton_profile_photo).setAttribute(MarkupTag.IMG.src(), ai.ilikeplaces.logic.Listeners.widgets.UserProperty.formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));
                            $(Skeleton_othersidebar_wall_link).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
                        }
                    }
                } catch (final Throwable t) {
                    EXCEPTION.error("{}", t);

                }
            }
        }

        sl.complete(LEVEL.DEBUG, Loggers.DONE);//Request completed within timeout. If not, goes to LEVEL.SERVER_STATUS
    }


    /**
     * Use ItsNatHTMLDocument variable stored in the AbstractListener class
     * Do not call this method anywhere, just implement it, as it will be
     * automatically called by the constructor
     * <p/>
     * If/when you are overriding this method, remember to call super.registerEventListeners(with required params)  so that,
     *
     * @param itsNatHTMLDocument_
     * @param itsNatDocument__
     * @param hTMLDocument_
     */
    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_, final ItsNatDocument itsNatDocument__) {
    }
}

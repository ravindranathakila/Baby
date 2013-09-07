package ai.baby.logic.Listeners;

import ai.baby.servlets.Controller;
import ai.baby.util.AbstractListener;
import ai.baby.util.ExceptionCache;
import ai.baby.util.Loggers;
import ai.baby.util.MarkupTag;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.baby.logic.Listeners.widgets.SignInOn;
import ai.baby.logic.Listeners.widgets.SignInOnCriteria;
import ai.baby.rbs.RBGet;
import ai.scribble.License;
import ai.scribble._fix;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jul 12, 2010
 * Time: 9:54:25 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@_fix("The protected methods to be used by extended classes an be used for setting values in this class. It seems the code is dupliated but need to be checked carefully before changing the code.")
abstract public class AbstractSkeletonListener extends AbstractListener {
    private static final String BN = "bn";
    protected static final ResourceBundle GUI = RBGet.gui();
    private static final String CODENAME_KEY = "codename";
    boolean initStatus = false;
    private static final String TALK = "Get in touch";

    /**
     * @param request_
     * @param response_
     */
    public AbstractSkeletonListener(final ItsNatServletRequest request_, final ItsNatServletResponse response_) {
        super(request_, response_, request_);
    }

    /**
     * Initializes your document here by appending fragments.
     * <p/>
     * If/when you are overriding this method, remember to call super.init(with required params)  so that,
     * the template is initialized. You have the flexibility as to when to call this method when doing so. i.e. Either
     * after you're done with your own init work, or before doing it, or in the middle!
     *
     * @param itsNatHTMLDocument__
     * @param hTMLDocument__
     * @param itsNatDocument__
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {
        if (initStatus) {
            throw ExceptionCache.MULTIPLE_INITS;
        }

        initStatus = true;

        final ItsNatServletRequest request__ = (ItsNatServletRequest) initArgs[0];

        //itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

        layoutNeededForAllPages:
        {
            setLoginWidget:
            {
                setLoginWidget(request__, SignInOnCriteria.SignInOnDisplayComponent.TALKS);
            }

            setTitle:
            {
                setTitle(RBGet.globalConfig.getString(CODENAME_KEY));
            }
            setProfileDataLink:
            {
                setProfileDataLink();
            }
            sideBarFriends:
            {
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
    }

    protected void setNotifications() {
        try {
            if (getUsername() != null) {
                //Set your logged in notifications here
            }
        } catch (final Throwable t) {
            Loggers.EXCEPTION.error("{}", t);
        }
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

    protected void setLoginWidget(final ItsNatServletRequest request__, final SignInOnCriteria.SignInOnDisplayComponent signInOnDisplayComponent) {
        initStatus = true;

        try {
            new SignInOn(request__, $(Controller.Page.Skeleton_login_widget), new SignInOnCriteria()
                    .setHumanId(new HumanId(getUsername()))
                    .setSignInOnDisplayComponent(signInOnDisplayComponent)) {
            };
        } catch (final Throwable t) {
            Loggers.EXCEPTION.error("{}", t);
        }
    }

    protected void setTitle(final String customTitle) {
        initStatus = true;

        try {
            setMainTitle:
            {
                if (customTitle != null) {
                    $(Controller.Page.skeletonTitle).setTextContent(customTitle);
                } else {
                    $(Controller.Page.skeletonTitle).setTextContent(RBGet.globalConfig.getString(BN));
                }
            }
            setMetaDescription:
            {
                if (customTitle != null) {
                    $(Controller.Page.skeletonTitle).setAttribute(MarkupTag.META.namee(), customTitle);
                } else {
                    $(Controller.Page.skeletonTitle).setAttribute(MarkupTag.META.namee(), RBGet.globalConfig.getString(BN));
                }
            }
        } catch (final Throwable t) {
            Loggers.DEBUG.debug(t.getMessage());
        }
    }


    protected void setProfileDataLink() {
        initStatus = true;

        try {
            if (getUsername() != null) {

                final HumansIdentity hi = ai.baby.logic.Listeners.widgets.UserProperty.HUMANS_IDENTITY_CACHE.get(getUsernameAsValid(), "");
                String profilePhotoURL = hi.getHumansIdentityProfilePhoto();
                $(Controller.Page.Skeleton_profile_photo).setAttribute(
                        MarkupTag.IMG.src(),
                        ai.baby.logic.Listeners.widgets.UserProperty.formatProfilePhotoUrlStatic(profilePhotoURL)
                );

                $(Controller.Page.Skeleton_othersidebar_identity).setTextContent(ai.baby.logic.Listeners.widgets.UserProperty.HUMANS_IDENTITY_CACHE.get(getUsername(), "").getHuman().getDisplayName());
            }
        } catch (final Throwable t) {
            Loggers.EXCEPTION.error("{}", t);
        }
    }


}

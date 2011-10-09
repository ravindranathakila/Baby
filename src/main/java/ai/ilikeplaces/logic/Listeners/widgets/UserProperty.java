package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.cache.SmartCache;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class UserProperty extends AbstractWidgetListener {
    private static final String USER_PROPERTY_EMAIL_XHTML = Controller.USER_PROPERTY_EMAIL_XHTML;
    private static final String YIKES__SOMETHING__WENT__WRONG = "YIKES_SOMETHING_WENT_WRONG";
    private static final String PROFILE__PHOTO__DEFAULT = "PROFILE_PHOTO_DEFAULT";
    private static final String INVITES_AVATAR_CONTAINER = "INVITES_AVATAR_CONTAINER";
    private static final String PROFILE__PHOTOS = "PROFILE_PHOTOS";
    private static final String WEBSITE = "WEBSITE";
    private static final String HASH = "#";
    private static final String EXT_GIF = ".gif";
    public static final String SENDER_NAME = "_senderName_";

    final static public SmartCache<String, HumansIdentity> HUMANS_IDENTITY_CACHE = new SmartCache<String, HumansIdentity>(new SmartCache.RecoverWith<String, HumansIdentity>() {

        @Override
        public HumansIdentity getValue(final String s) {
            return DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(new HumanId(s)).returnValue();
        }
    });

    /**
     * Shows the profile belonging to humanId
     * <p/>
     * Does not support fetchtoemail. use other constructor instead.
     * <p/>
     * Use this if you are not using fetchtomail.
     *
     * @param request__
     * @param appendToElement__
     * @param humanIdWhosProfileToShow
     * @param params
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanIdWhosProfileToShow, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, humanIdWhosProfileToShow, params);
        try {
            final HumansIdentity hi = HUMANS_IDENTITY_CACHE.get(new String(humanIdWhosProfileToShow.getHumanId()));

            $$(Page.user_property_name).setTextContent(hi.getHuman().getDisplayName());
            $$(Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            $$(Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));

            /*  fetchToEmail(//WARNING! This does not append the content.
            hi.getHuman().getInviterDisplayName(),
            formatProfileUrl(ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl(), true),
            formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));*/

        } catch (final Throwable t) {
            Loggers.ERROR.error(STR_FATAL_ERROR_IN_WIDGET_THIS_SHOULD_NOT_HAPPEN_DETAILS_AS_FOLLOWS +
                    STR_PAGE + Page.UserProperty +
                    STR_APPEND_TO_ELEMENT + (appendToElement__ != null ? appendToElement__.getAttribute(MarkupTag.GENERIC.id()) : null) +
                    STR_HUMAN_ID + getHumanIdFromRequest(request__), t);
        }
    }

    /**
     * Shows the profile belonging to humanId
     *
     * @param request__
     * @param appendToElement__
     * @param content
     * @param humanIdWhosProfileToShow
     * @param params
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final Element content, final HumanId humanIdWhosProfileToShow, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, humanIdWhosProfileToShow, params);
        try {
            final HumansIdentity hi = HUMANS_IDENTITY_CACHE.get(new String(humanIdWhosProfileToShow.getHumanId()));


            $$(Page.user_property_name).setTextContent(hi.getHuman().getDisplayName());
            $$(Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            $$(Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));
            $$(Page.user_property_content).appendChild(content);

            this.fetchToEmail(
                    hi.getHuman().getDisplayName(),
                    formatProfileUrl(ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl(), true),
                    formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()),
                    content); //http://blog.ilikeplaces.com/

        } catch (final Throwable t) {
            Loggers.ERROR.error(STR_FATAL_ERROR_IN_WIDGET_THIS_SHOULD_NOT_HAPPEN_DETAILS_AS_FOLLOWS +
                    STR_PAGE + Page.UserProperty +
                    STR_APPEND_TO_ELEMENT + (appendToElement__ != null ? appendToElement__.getAttribute(MarkupTag.GENERIC.id()) : null) +
                    STR_HUMAN_ID + getHumanIdFromRequest(request__), t);
        }
    }

    /**
     * Shows the profile belonging to humanId
     *
     * @param request__
     * @param appendToElement__
     * @param content
     * @param humanIdWhosProfileToShow
     * @param params
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final Element content, final HumansIdentity humanIdWhosProfileToShow, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, humanIdWhosProfileToShow, params);

        try {
            $$(Page.user_property_name).setTextContent(humanIdWhosProfileToShow.getHuman().getDisplayName());
            $$(Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + humanIdWhosProfileToShow.getUrl().getUrl());
            $$(Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfilePhotoUrl(humanIdWhosProfileToShow.getHumansIdentityProfilePhoto()));
            $$(Page.user_property_content).appendChild(content);

            this.fetchToEmail(
                    humanIdWhosProfileToShow.getHuman().getDisplayName(),
                    formatProfileUrl(ProfileRedirect.PROFILE_URL + humanIdWhosProfileToShow.getUrl().getUrl(), true),
                    formatProfilePhotoUrl(humanIdWhosProfileToShow.getHumansIdentityProfilePhoto()),
                    content); //http://blog.ilikeplaces.com/
        } catch (final Throwable t) {
            Loggers.ERROR.error(STR_FATAL_ERROR_IN_WIDGET_THIS_SHOULD_NOT_HAPPEN_DETAILS_AS_FOLLOWS +
                    STR_PAGE + Page.UserProperty +
                    STR_APPEND_TO_ELEMENT + (appendToElement__ != null ? appendToElement__.getAttribute(MarkupTag.GENERIC.id()) : null) +
                    STR_HUMAN_ID + getHumanIdFromRequest(request__), t);
        }
    }

    /**
     * General purpose user property renderer with NO CONNECTION TO DATABASE WHATSOEVER
     *
     * @param request__
     * @param appendToElement__
     * @param content
     * @param inviteCriteria
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final Element content, final InviteCriteria inviteCriteria) {
        super(request__, Page.UserProperty, appendToElement__, content, inviteCriteria);

        try {
            $$(Page.user_property_name).setTextContent(inviteCriteria.getInvitee().getFullName());
            $$(Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + inviteCriteria.getProfileUrl());
            $$(Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfilePhotoUrl(inviteCriteria.getProfilePhoto()));
            $$(Page.user_property_content).appendChild(content);

            this.fetchToEmail(
                    inviteCriteria.getInviterDisplayName(),
                    "#",
                    formatProfilePhotoUrl(inviteCriteria.getProfilePhoto()),
                    content); //http://blog.ilikeplaces.com/
        } catch (final Throwable t) {
            Loggers.ERROR.error(STR_FATAL_ERROR_IN_WIDGET_THIS_SHOULD_NOT_HAPPEN_DETAILS_AS_FOLLOWS +
                    STR_PAGE + Page.UserProperty +
                    STR_APPEND_TO_ELEMENT + (appendToElement__ != null ? appendToElement__.getAttribute(MarkupTag.GENERIC.id()) : null) +
                    STR_HUMAN_ID + getHumanIdFromRequest(request__), t);
        }
    }

    /**
     * @param args
     */
    protected void fetchToEmail(final Object... args) {
        try {
            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + USER_PROPERTY_EMAIL_XHTML);
            $$(Controller.Page.user_property_name, document).setTextContent((String) args[0]);
            $$(Controller.Page.user_property_name, document).setAttribute(MarkupTag.A.href(), (String) args[1]);
            $$(Controller.Page.user_property_profile_photo, document).setAttribute(MarkupTag.IMG.src(), (String) args[2]);
            $$(Controller.Page.user_property_content, document).appendChild(
                    document.importNode(((Element) args[3]), true)
            );


            fetchToEmail = HTMLDocParser.convertNodeToHtml($$(Controller.Page.user_property_widget, document));
        } catch (final TransformerException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final SAXException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final IOException e) {
            Loggers.EXCEPTION.error("", e);
        }
    }

    /**
     * You can also use {@link #getUserPropertyHtmlFor(ai.ilikeplaces.logic.validators.unit.HumanId, String, String)}
     *
     * @param usersName
     * @param usersUrl
     * @param usersPhoto
     * @param content
     * @return String content or null if something goes wrong
     */
    public static String fetchToEmailStatically(final String usersName, final String usersUrl, final String usersPhoto, final Element content) {
        try {
            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + USER_PROPERTY_EMAIL_XHTML);
            document.getElementById(Controller.Page.user_property_name).setTextContent(usersName);
            document.getElementById(Controller.Page.user_property_name).setAttribute(MarkupTag.A.href(), usersUrl);
            document.getElementById(Controller.Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), usersPhoto);
            document.getElementById(Controller.Page.user_property_content).appendChild(document.importNode(content, true));

            return HTMLDocParser.convertNodeToHtml(document);
        } catch (final TransformerException e) {
            Loggers.EXCEPTION.error("", e);
            return null;
        } catch (final SAXException e) {
            Loggers.EXCEPTION.error("", e);
            return null;
        } catch (final IOException e) {
            Loggers.EXCEPTION.error("", e);
            return null;
        }
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
    }

    /**
     * User profile photo URL. Can be null, empty or # in which case the default photo URL is returned. This value is
     * define in the properties files
     *
     * @param profileUrl
     * @return
     */
    static public String formatProfilePhotoUrl(final String profileUrl) {
        return profileUrl == null || profileUrl.isEmpty() || profileUrl.equals(HASH) ?
                RBGet.globalConfig.getString(INVITES_AVATAR_CONTAINER) + (((((int) (Math.random() * 100)) % 16)) + 1) + EXT_GIF :
                RBGet.globalConfig.getString(PROFILE__PHOTOS) + profileUrl;
    }

    static public String formatProfilePhotoUrlStatic(final String profileUrl) {
        return profileUrl == null || profileUrl.isEmpty() ?
                RBGet.getGlobalConfigKey(PROFILE__PHOTO__DEFAULT) :
                RBGet.getGlobalConfigKey(PROFILE__PHOTOS) + profileUrl;
    }

    static public String formatProfileUrl(final String relativeURL, final boolean makeAbsolute) {
        return makeAbsolute ? RBGet.globalConfig.getString(WEBSITE) + relativeURL : relativeURL;
    }

    /**
     * Online, implies that the receiver is online
     * You can also use {@link #fetchToEmailStatically(String, String, String, org.w3c.dom.Element)}
     *
     * @param sender
     * @param receiver
     * @param withNonHtmlContent Use _senderName_ identified by {@link #SENDER_NAME} in places you need the display name of the sender to be addd
     * @return
     */
    final static public String getUserPropertyHtmlFor(final HumanId sender, final String receiver, final String withNonHtmlContent) {
        try {
            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + Controller.USER_PROPERTY_EMAIL_XHTML);

            final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(sender);
            final HumansIdentity hi = r.returnValue();

            final String displayName = hi.getHuman().getDisplayName();
            $$static(Controller.Page.user_property_name, document).setTextContent(displayName);
            $$static(Controller.Page.user_property_name, document).setAttribute(MarkupTag.A.href(),
                    "http://www.ilikeplaces.com" + ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            $$static(Controller.Page.user_property_profile_photo, document).setAttribute(MarkupTag.IMG.src(),
                    formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));


            $$static(Controller.Page.user_property_content, document).appendChild(
                    document.importNode(
                            ElementComposer.compose(
                                    document.createElement(MarkupTag.DIV.toString())
                            ).$ElementSetText(
                                    withNonHtmlContent.replace(SENDER_NAME, displayName)
                            ).getAsNode(),
                            true)
            );


            return HTMLDocParser.convertNodeToHtml($$static(Page.user_property_widget, document));
        } catch (final Throwable e) {
            throw LogNull.getRuntimeException(e);
        }
    }

    /**
     * Online, implies that the receiver is online
     * You can also use {@link #fetchToEmailStatically(String, String, String, org.w3c.dom.Element)}
     *
     * @param sender
     * @param receiver
     * @param htmlContent
     * @return
     */
    final static public String getUserPropertyHtmlFor(final HumanId sender, final String receiver, final Element htmlContent) {
        try {
            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + Controller.USER_PROPERTY_EMAIL_XHTML);

            final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(sender);
            final HumansIdentity hi = r.returnValue();

            final String displayName = hi.getHuman().getDisplayName();
            $$static(Controller.Page.user_property_name, document).setTextContent(displayName);
            $$static(Controller.Page.user_property_name, document).setAttribute(MarkupTag.A.href(),
                    "http://www.ilikeplaces.com" + ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            $$static(Controller.Page.user_property_profile_photo, document).setAttribute(MarkupTag.IMG.src(),
                    formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));


            $$static(Controller.Page.user_property_content, document).appendChild(
                    document.importNode(
                            ElementComposer.compose(
                                    document.createElement(MarkupTag.DIV.toString())
                            ).wrapThis(
                                    (Element) document.importNode(htmlContent, true)
                            ).getAsNode(),
                            true)
            );


            return HTMLDocParser.convertNodeToHtml($$static(Page.user_property_widget, document));
        } catch (final Throwable e) {
            throw LogNull.getRuntimeException(e);
        }
    }
}
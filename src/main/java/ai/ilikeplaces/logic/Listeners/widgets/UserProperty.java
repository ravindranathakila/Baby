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
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class UserProperty extends AbstractWidgetListener {

    private static final String USER_PROPERTY_EMAIL_XHTML = "ai/ilikeplaces/widgets/UserProperty_email.xhtml";
    private static final String YIKES__SOMETHING__WENT__WRONG = "YIKES_SOMETHING_WENT_WRONG";
    private static final String PROFILE__PHOTO__DEFAULT = "PROFILE_PHOTO_DEFAULT";
    private static final String PROFILE__PHOTOS = "PROFILE_PHOTOS";
    private static final String WEBSITE = "WEBSITE";

    /**
     * Shows the profile belonging to humanId
     * <p/>
     * Does not support fetchtoemail. use other constructor instead.
     * <p/>
     * Use this if you are not using fetchtomail.
     *
     * @param request__
     * @param appendToElement__
     * @param humanId
     * @param params
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, humanId, params);
        final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(humanId);
        if (r.returnStatus() == 0) {
            final HumansIdentity hi = r.returnValue();
            $$(Controller.Page.user_property_name).setTextContent(hi.getHuman().getDisplayName());
            $$(Controller.Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            $$(Controller.Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));

            /*  fetchToEmail(//WARNING! This does not append the content.
            hi.getHuman().getDisplayName(),
            formatProfileUrl(ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl(), true),
            formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));*/
        } else {
            final String error = RBGet.gui().getString(YIKES__SOMETHING__WENT__WRONG);
            $$(Controller.Page.user_property_name).setTextContent(error);
        }
    }

    /**
     * Shows the profile belonging to humanId
     *
     * @param request__
     * @param appendToElement__
     * @param content
     * @param humanId
     * @param params
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final Element content, final HumanId humanId, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, humanId, params);
        final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(humanId);
        if (r.returnStatus() == 0) {
            final HumansIdentity hi = r.returnValue();
            $$(Controller.Page.user_property_name).setTextContent(hi.getHuman().getDisplayName());
            $$(Controller.Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            $$(Controller.Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));
            $$(Controller.Page.user_property_content).appendChild(content);

            this.fetchToEmail(
                    hi.getHuman().getDisplayName(),
                    formatProfileUrl(ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl(), true),
                    formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()),
                    content); //http://blog.ilikeplaces.com/
        } else {
            final String error = RBGet.gui().getString(YIKES__SOMETHING__WENT__WRONG);
            $$(Controller.Page.user_property_name).setTextContent(error);
        }
    }

    /**
     * Shows the profile belonging to humanId
     *
     * @param request__
     * @param appendToElement__
     * @param content
     * @param humansIdentity
     * @param params
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final Element content, final HumansIdentity humansIdentity, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, humansIdentity, params);
        $$(Controller.Page.user_property_name).setTextContent(humansIdentity.getHuman().getDisplayName());
        $$(Controller.Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + humansIdentity.getUrl().getUrl());
        $$(Controller.Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfilePhotoUrl(humansIdentity.getHumansIdentityProfilePhoto()));
        $$(Controller.Page.user_property_content).appendChild(content);

        this.fetchToEmail(
                humansIdentity.getHuman().getDisplayName(),
                formatProfileUrl(ProfileRedirect.PROFILE_URL + humansIdentity.getUrl().getUrl(), true),
                formatProfilePhotoUrl(humansIdentity.getHumansIdentityProfilePhoto()),
                content); //http://blog.ilikeplaces.com/

    }

    /**
     * General purpose user property renderer with NO CONNECTION TO DATABASE WHATSOEVER
     *
     * @param displayName
     * @param profileUrl
     * @param profilePhoto
     * @param content
     * @param params
     */
    public UserProperty(final ItsNatServletRequest request__, final Element appendToElement__, final String displayName, final String profileUrl, final String profilePhoto, final Element content, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, params);
        $$(Controller.Page.user_property_name).setTextContent(displayName);
        $$(Controller.Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + profileUrl);
        $$(Controller.Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), profilePhoto);
        $$(Controller.Page.user_property_content).appendChild(content);

        this.fetchToEmail(
                params,
                profileUrl,
                profilePhoto,
                content); //http://blog.ilikeplaces.com/

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


    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }

    /**
     * User profile photo URL from DB. Can be null, in which case the default photo URL is returned. This value is
     * define in the properties files
     *
     * @param rawURLFromDB
     * @return
     */
    static public String formatProfilePhotoUrl(final String rawURLFromDB) {
        return rawURLFromDB == null || rawURLFromDB.isEmpty() ?
                RBGet.globalConfig.getString(PROFILE__PHOTO__DEFAULT) :
                RBGet.globalConfig.getString(PROFILE__PHOTOS) + rawURLFromDB;
    }

    static public String formatProfileUrl(final String relativeURL, final boolean makeAbsolute) {
        return makeAbsolute ? RBGet.globalConfig.getString(WEBSITE) + relativeURL : relativeURL;
    }
}
package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.cache.SmartCache2String;
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
abstract public class UserPropertySidebar extends AbstractWidgetListener {

    private static final String USER_PROPERTY_EMAIL_XHTML = "ai/ilikeplaces/widgets/UserProperty_email.xhtml";
    protected String profilePhotoURLFinal = "#";

    public final static SmartCache2String<HumansIdentity, String> HUMANS_IDENTITY_SIDEBAR_CACHE = UserProperty.HUMANS_IDENTITY_CACHE;

    public static enum UserPropertySidebarIds implements WidgetIds {
        user_property_sidebar_profile_photo,
        user_property_sidebar_name,
        user_property_sidebar_widget,
        user_property_sidebar_content,
        user_property_sidebar_talk,
        user_property_sidebar_engage,
        user_property_sidebar_name_section,
    }


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
    public UserPropertySidebar(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId, final Object... params) {
        super(request__, Page.UserPropertySidebar, appendToElement__, humanId, params);

        final HumansIdentity hi = HUMANS_IDENTITY_SIDEBAR_CACHE.get((humanId.getHumanId()),"");

        $$(UserPropertySidebarIds.user_property_sidebar_name).setTextContent(hi.getHuman().getDisplayName());
        //$$(Page.user_property_sidebar_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
        $$(UserPropertySidebarIds.user_property_sidebar_profile_photo).setAttribute(MarkupTag.IMG.title(), profilePhotoURLFinal = formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));

        /*  fetchToEmail(//WARNING! This does not append the content.
        hi.getHuman().getInviterDisplayName(),
        formatProfileUrl(ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl(), true),
        formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));*/

    }

    /**
     * Shows the profile belonging to humanId
     *
     * @param request__
     * @param appendToElement__
     * @param humanId
     * @param params
     */
    public UserPropertySidebar(final ItsNatServletRequest request__, final Element appendToElement__, final Element content, final HumanId humanId, final Object... params) {
        super(request__, Page.UserProperty, appendToElement__, humanId, params);
        final HumansIdentity hi = HUMANS_IDENTITY_SIDEBAR_CACHE.get((humanId.getHumanId()),"");
        $$(UserPropertySidebarIds.user_property_sidebar_name).setTextContent(hi.getHuman().getDisplayName());
        //$$(Page.user_property_sidebar_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
        $$(UserPropertySidebarIds.user_property_sidebar_profile_photo).setAttribute(MarkupTag.IMG.title(), profilePhotoURLFinal = formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()));
        $$(UserPropertySidebarIds.user_property_sidebar_content).appendChild(content);

        fetchToEmail(
                hi.getHuman().getDisplayName(),
                formatProfileUrl(ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl(), true),
                formatProfilePhotoUrl(hi.getHumansIdentityProfilePhoto()),
                content);

    }

    /**
     * @param args
     */
    protected void fetchToEmail(final Object... args) {
        try {
            final Document document = HTMLDocParser.getDocument(Controller.REAL_PATH + Controller.WEB_INF_PAGES + USER_PROPERTY_EMAIL_XHTML);
            $$(UserPropertySidebarIds.user_property_sidebar_name.name(), document).setTextContent((String) args[0]);
            //$$(Page.user_property_sidebar_name, document).setAttribute(MarkupTag.A.href(), (String) args[1]);
            $$(UserPropertySidebarIds.user_property_sidebar_profile_photo.name(), document).setAttribute(MarkupTag.IMG.src(), (String) args[2]);
            $$(UserPropertySidebarIds.user_property_sidebar_content.name(), document).appendChild(
                    document.importNode(((Element) args[3]), true)
            );


            fetchToEmail = HTMLDocParser.convertNodeToHtml($$(UserPropertySidebarIds.user_property_sidebar_widget.name(), document));
        } catch (final TransformerException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final SAXException e) {
            Loggers.EXCEPTION.error("", e);
        } catch (final IOException e) {
            Loggers.EXCEPTION.error("", e);
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
     * User profile photo URL from DB. Can be null, in which case the default photo URL is returned. This value is
     * define in the properties files
     *
     * @param rawURLFromDB
     * @return
     */
    static public String formatProfilePhotoUrl(final String rawURLFromDB) {
        return UserProperty.formatProfilePhotoUrl(rawURLFromDB);
    }

    static public String formatProfileUrl(final String relativeURL, final boolean makeAbsolute) {
        return UserProperty.formatProfileUrl(relativeURL, makeAbsolute);
    }
}
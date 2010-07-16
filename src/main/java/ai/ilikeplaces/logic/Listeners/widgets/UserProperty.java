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
import org.itsnat.core.ItsNatDocument;
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

    private static final String USER_PROPERTY_EMAIL_XHTML = "UserProperty_email.xhtml";

    /**
     * Shows the profile belonging to humanId
     *
     * @param itsNatDocument__
     * @param appendToElement__
     * @param humanId
     * @param params
     */
    public UserProperty(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId, final Object... params) {
        super(itsNatDocument__, Page.UserProperty, appendToElement__, humanId, params);
        final Return<HumansIdentity> r = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansIdentity(humanId);
        if (r.returnStatus() == 0) {
            final HumansIdentity hi = r.returnValue();
            $$(Controller.Page.user_property_name).setTextContent(hi.getHuman().getDisplayName());
            $$(Controller.Page.user_property_name).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            $$(Controller.Page.user_property_profile_photo).setAttribute(MarkupTag.IMG.src(), formatProfileUrl(hi.getHumansIdentityProfilePhoto()));

            fetchToEmail(hi.getHuman().getDisplayName(), MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl(), formatProfileUrl(hi.getHumansIdentityProfilePhoto()));
        } else {
            final String error = RBGet.gui().getString("YIKES_SOMETHING_WENT_WRONG");
            $$(Controller.Page.user_property_name).setTextContent(error);
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

            fetchToEmail = HTMLDocParser.convertNodeToHtml($$(Page.user_property_widget));
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
    static public String formatProfileUrl(final String rawURLFromDB) {
        return rawURLFromDB == null || rawURLFromDB.isEmpty() ?
                RBGet.globalConfig.getString("PROFILE_PHOTO_DEFAULT") :
                RBGet.globalConfig.getString("PROFILE_PHOTOS") + rawURLFromDB;
    }
}
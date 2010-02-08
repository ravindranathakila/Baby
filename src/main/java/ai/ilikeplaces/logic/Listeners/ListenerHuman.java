package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.logic.Listeners.widgets.PhotoCRUD;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractListener;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;
import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
@TODO(task = "try/catch all interface elements")
public class ListenerHuman implements ItsNatServletRequestListener {

    final private Logger logger = LoggerFactory.getLogger(ListenerHuman.class.getName());

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor + JSCodeToSend.FnLocationId);
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");
                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        new SignInOn(itsNatDocument__, $(Main_login_widget), new HumanId(getUsername())) {
                        };
                    }

                    setMainTitle:
                    {
                        try {
                            //$(mainTitle).setTextContent("Welcome to ilikeplaces!");
                        } catch (
                                @FIXME(issue = "This is very important for SEO. Contact ItsNat and find out why exception always occurs here. It says element not found.")
                                final Exception e__) {
                            logger.debug(e__.getMessage());
                        }
                    }
                    signOnDisplayLink:
                    {
                        if (getUsername() != null) {
                            final Element usersName = $(MarkupTag.P);
                            usersName.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsername());
                            $(Main_othersidebar_identity).appendChild(usersName);
                        } else {
                            final Element locationElem = $(MarkupTag.P);
                            locationElem.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0005") + location);
                            $(Main_othersidebar_identity).appendChild(locationElem);
                        }

                    }
                    setProfileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                $(Main_othersidebar_profile_link).setAttribute("href", Controller.Page.home.getURL());
                            } else {
                                $(Main_othersidebar_profile_link).setAttribute("href", Controller.Page.signup.getURL());
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }
                }
                if (getUsername() != null) {

                    Return<List<PublicPhoto>> r = DB.getHumanCRUDPublicPhotoLocal(true).rPublicPhoto(getUsername());

                    if (r.returnStatus() == 0) {
                        for (final PublicPhoto publicPhoto : r.returnValue()) {
                            new PhotoCRUD(itsNatDocument__, $(Controller.Page.Main_center_main), publicPhoto, getUsername()) {

                                @Override
                                protected void init(final Object... initArgs) {

                                    $$(pc_photo_permalink).setAttribute(MarkupTag.A.href(), "");
                                    $$(pc_photo).setAttribute(MarkupTag.A.src(), publicPhoto.getPublicPhotoURLPath());
                                    $$(pc_photo_description).setAttribute(MarkupTag.TEXTAREA.value(), publicPhoto.getPublicPhotoDescription());
                                }
                            };
                        }
                    } else {
                        $(Main_notice).setTextContent(r.returnMsg());
                    }

                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
            }
        };
    }
}

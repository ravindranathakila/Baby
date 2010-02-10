package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.logic.Listeners.widgets.Photo$Description;
import ai.ilikeplaces.logic.Listeners.widgets.PrivateLocationCreate;
import ai.ilikeplaces.logic.Listeners.widgets.PrivateLocationDelete;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@TODO(task = "RENAME TO LISTENERLOCATION. DO A STRING SEARCH ON LISTENERMAIN TO FIND USAGE FIRST. CURRENT SEARCH SHOWS NO ISSUES. REFAC DELAYED TILL NEXT CHECK")
public class ListenerMain implements ItsNatServletRequestListener {


    final static private Logger logger = LoggerFactory.getLogger(ListenerMain.class);
    final static protected String LocationId = RBGet.config.getString("LOCATIONID");

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
            @TODO(task = "If location is not available, it should be added through a widget(or fragment maybe?)")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor + JSCodeToSend.FnLocationId + JSCodeToSend.FnLocationName + JSCodeToSend.FnSetTitle);
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");

                logTheQueryLocationPerUser:
                {
                    if (getUsername() != null) {
                        Loggers.USER.info(getUsername() + " queries for location " + location);
                    } else {
                        Loggers.NON_USER.info("queries for location " + location);
                    }
                }

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        try {
                            new SignInOn(itsNatDocument__, $(Main_login_widget), new HumanId(getUsername())) {
                            };
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    setMainTitle:
                    {
                        try {
                            //$(mainTitle).setTextContent("Welcome to ilikeplaces!");
                        } catch (
                                @FIXME(issue = "Once its nat final release is made, set head as nocache and set. then works.")
                                final Exception e__) {
                            logger.debug(e__.getMessage());
                        }
                    }
                    signOnDisplayLink:
                    {
                        try {
                            if (getUsername() != null) {
                                final Element usersName = $(MarkupTag.P);
                                usersName.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsername());
                                $(Main_othersidebar_identity).appendChild(usersName);
                            } else {
                                final Element locationElem = $(MarkupTag.P);
                                locationElem.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0005") + location);
                                $(Main_othersidebar_identity).appendChild(locationElem);
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
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

                final Return<Location> r = DB.getHumanCRUDLocationLocal(true).dirtyRLocation(location, null);


                if (r.returnStatus() == 0 && r.returnValue() != null) {
                    final Location existingLocation_ = r.returnValue();

                    showUploadFileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                displayBlock($(Main_othersidebar_upload_file_sh));
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    setLocationIdForJSReference:
                    {
                        try {
                            final Element hiddenLocationIdInputTag = $(MarkupTag.INPUT);
                            hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.type(), MarkupTag.INPUT.typeValueHidden());
                            hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.id(), JSCodeToSend.LocationId);
                            hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.value(), existingLocation_.getLocationId().toString());
                            hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    setLocationNameForJSReference:
                    {
                        try {
                            @TODO(task = "MOVE TO GUI MSGS I18N METHODICALLY")
                            final Element hiddenLocationIdInputTag = $(MarkupTag.INPUT);
                            hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.type(), MarkupTag.INPUT.typeValueHidden());
                            hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.id(), JSCodeToSend.LocationName);
                            hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.value(),
                                    "This is " + existingLocation_.getLocationName() + " of " + existingLocation_.getLocationSuperSet().getLocationName() + ". " +
                                            "You are at " + existingLocation_.getLocationName() + " in " + existingLocation_.getLocationSuperSet().getLocationName() + ". ");
                            hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    setLocationAsTitle:
                    {
                        try {
                            final String pageTitle = existingLocation_.getLocationName();
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    setLocationAsPageTopic:
                    {
                        try {
                            $(Main_center_main_location_title).setTextContent("This is " + existingLocation_.getLocationName() + " of " + existingLocation_.getLocationSuperSet());
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    getAndDisplayAllThePhotos:
                    {
                        List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
                        logger.info(RBGet.logMsgs.getString("NUMBER_OF_PHOTOS_FOR_LOCATION"), existingLocation_.getLocationName(), listPublicPhoto.size());

                        @TODO(task = "INVERT COLORS AND SET PHOTO NAME")
                        int i = 0;
                        for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {

                            try {
                                final PublicPhoto publicPhoto = it.next();
                                if (i % 2 == 0) {
                                    new Photo$Description(itsNatDocument__, $(Main_center_main)) {
                                        @Override
                                        protected void init(final Object... initArgs) {
                                            $$(pd_photo_permalink).setAttribute(MarkupTag.A.href(), publicPhoto.getPublicPhotoURLPath() + "|" + publicPhoto.getPublicPhotoName());
                                            $$(pd_photo).setAttribute(MarkupTag.A.src(), publicPhoto.getPublicPhotoURLPath());
                                            //final Element descriptionText = $$(MarkupTag.P);
                                            //descriptionText.setTextContent(publicPhoto.getPublicPhotoDescription());
                                            $$(pd_photo_description).setTextContent(publicPhoto.getPublicPhotoDescription());
                                        }
                                    };
                                } else {
                                    new Photo$Description(itsNatDocument__, $(Main_center_main)) {
                                        @Override
                                        protected void init(final Object... initArgs) {
                                            $$(pd_photo_permalink).setAttribute(MarkupTag.A.href(), publicPhoto.getPublicPhotoURLPath() + "|" + publicPhoto.getPublicPhotoName());
                                            $$(pd_photo).setAttribute(MarkupTag.A.src(), publicPhoto.getPublicPhotoURLPath());
                                            //final Element descriptionText = $$(MarkupTag.P);
                                            //descriptionText.setTextContent(publicPhoto.getPublicPhotoDescription());
                                            $$(pd_photo_description).setTextContent(publicPhoto.getPublicPhotoDescription());

                                        }
                                    };
                                }
                            } catch (final Throwable t) {
                                Loggers.EXCEPTION.error("", t);
                            }
                        }
                    }

                } else {
                    noSupportForNewLocationsYet:
                    {
                        try {
                            $(Main_notice).appendChild(($(MarkupTag.P).appendChild(
                                    hTMLDocument__.createTextNode(RBGet.logMsgs.getString("CANT_FIND_LOCATION")
                                            + " Were you looking for "
                                    ))));
                            DB.getHumanCRUDLocationLocal(true).dirtyRLikeLocationName(location);
                            for (final Element element : generateLocationLinks(DB.getHumanCRUDLocationLocal(true).dirtyRLikeLocationName(location))) {
                                $(Main_notice).appendChild(element);
                                displayBlock($(Main_notice_sh));
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }
                }

            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners
                    (
                            final ItsNatHTMLDocument itsNatHTMLDocument__,
                            final HTMLDocument hTMLDocument__,
                            final ItsNatDocument itsNatDocument__) {
//                ItsNatDocument itsNatDoc = itsNatDocument__;
//                Document docEvent = (Document) itsNatDoc;
//
//                MouseEvent mouseEvent = (MouseEvent) itsNatDoc.createEvent("MouseEvent");
//
//                mouseEvent.initMouseEvent("click", true, true, ((DocumentView) itsNatDoc).getDefaultView(), 0,
//                        0, 0, 0, 0, false, false, false, false, (short) 0/*left button*/, null);
//
//                itsNatDoc.dispatchEvent((EventTarget) $(Main_notice), mouseEvent);


            }

            private List<Element> generateLocationLinks
                    (
                            final List<String> locationList) {

                List<Element> links = new ArrayList<Element>();

                for (String locationName : locationList) {
                    final Element link = $(MarkupTag.A);
                    link.setAttribute(MarkupTag.A.href(), "/page/" + locationName);
                    link.setTextContent(locationName);
                    link.setAttribute(MarkupTag.A.alt(), "/page/" + locationName);
                    final Element linkDiv = $(MarkupTag.DIV);
                    linkDiv.appendChild(link);
                    links.add(linkDiv);
                }

                return links;

            }
        }

                ;
    }
}

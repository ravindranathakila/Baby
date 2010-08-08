package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
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
import static ai.ilikeplaces.util.Loggers.EXCEPTION;
import static ai.ilikeplaces.util.MarkupTag.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@TODO(task = "RENAME TO LISTENERLOCATION. DO A STRING SEARCH ON LISTENERMAIN TO FIND USAGE FIRST. CURRENT SEARCH SHOWS NO ISSUES. REFAC DELAYED TILL NEXT CHECK")
public class ListenerMain implements ItsNatServletRequestListener {


    final static protected String LocationId = RBGet.globalConfig.getString("LOCATIONID");
    private static final String RETURNING_LOCATION = "Returning location ";
    private static final String TO_USER = " to user";
    private static final String WRONG_WOEID_FORMAT = "SORRY! WRONG WOEID FORMAT";
    public static final String NUMBER_OF_PHOTOS_FOR = "Number of photos for ";
    public static final String COLON = ":";
    private static final String HTTP_SESSION_ATTR_LOCATION = "HttpSessionAttr.location";

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__) {

            protected String location;

            protected String superLocation;

            protected Long WOEID;

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            @TODO(task = "If location is not available, it should be added through a widget(or fragment maybe?)")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__, final Object... initArgs) {

                checkUserSpentTime:
                {
                    itsNatHTMLDocument__.addEventListener((EventTarget) $(Controller.Page.body), EventType.UNLOAD.toString(), new EventListener() {

                        Obj<Long> timeSpent = new Obj<Long>(System.currentTimeMillis());

                        @Override
                        public void handleEvent(final Event evt_) {
                            Loggers.DEBUG.debug("Unloading body. Time spent:" + (System.currentTimeMillis() - timeSpent.getObj()));
                        }

                        @Override
                        public void finalize() throws Throwable {
                            Loggers.finalized(this.getClass().getName());
                            super.finalize();
                        }
                    }, false);

                }


                //this.location = (String) request_.getServletRequest().getAttribute(RBGet.config.getString("HttpSessionAttr.location"));
                getLocationSuperLocation:
                {
                    final String[] attr = ((String) request__.getServletRequest().getAttribute(RBGet.globalConfig.getString(HTTP_SESSION_ATTR_LOCATION))).split("_");
                    location = attr[0];
                    if (attr.length == 3) {
                        superLocation = attr[2];
                    } else {
                        superLocation = "";
                    }
                    tryLocationId:
                    {
                        try {
                            final String w = request__.getServletRequest().getParameter(Location.WOEID);
                            if (w != null) {
                                WOEID = Long.parseLong(request__.getServletRequest().getParameter(Location.WOEID));
                            }
                        } catch (final NumberFormatException e) {
                            Loggers.USER_EXCEPTION.error(WRONG_WOEID_FORMAT, e);
                        }
                    }
                }

                final SmartLogger sl = SmartLogger.start(
                        Loggers.LEVEL.SERVER_STATUS,
                        RETURNING_LOCATION + location + TO_USER,
                        60000,
                        null,
                        true
                );

                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor + JSCodeToSend.FnLocationId + JSCodeToSend.FnLocationName + JSCodeToSend.FnSetTitle);
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");

                logTheQueryLocationPerUser:
                {
                    if (getUsername() != null) {
                        Loggers.USER.info(getUsernameAsValid() + " queries for location " + location + " of " + superLocation);
                        sl.appendToLogMSG(getUsernameAsValid());
                    } else {
                        Loggers.NON_USER.info(request__.getServletRequest().getRemoteHost() + " queries for location " + location + " of " + superLocation);
                        sl.appendToLogMSG(request__.getServletRequest().getRemoteHost());
                    }
                }

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        try {
                            new SignInOn(request__, $(Main_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                            };
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    SEO:
                    {
                        try {
                            setMainTitle:
                            {
                                $(mainTitle).setTextContent("Traveling to " + location + "? Reserve A Flight, Book A Hotel, and You're All Set!");

                            }
                            setMetaDescription:
                            {
                                $(mainMetaDesc).setAttribute(MarkupTag.META.content(), "Do a flight booking to " + location + " and find a hotel to stay. Hire a car to travel around. Click here to check for offers.");
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
                                $(Main_othersidebar_identity).setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsernameAsValid());
                            } else {
                                $(Main_othersidebar_identity).setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0005") + location);
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }

                    }
                    setProfileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                $(Main_othersidebar_profile_link).setAttribute("href", Controller.Page.Profile.getURL());
                            } else {
                                $(Main_othersidebar_profile_link).setAttribute("href", Controller.Page.signup.getURL());
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }
                    setProfilePhotoLink:
                    {
                        try {
                            if (getUsername() != null) {
                                /**
                                 * TODO check for db failure
                                 */
                                String url = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansProfilePhoto(new HumanId(getUsernameAsValid())).returnValueBadly();
                                url = url == null ? null : RBGet.globalConfig.getString("PROFILE_PHOTOS") + url;
                                if (url != null) {
                                    $(Main_profile_photo).setAttribute(MarkupTag.IMG.src(), url);
//                                    displayBlock($(Main_profile_photo));
//                                    displayNone($(Main_location_photo));
                                } else {
//                                    displayNone($(Main_profile_photo));
//                                    displayBlock($(Main_location_photo));
                                }
                            } else {
//                                displayNone($(Main_profile_photo));
//                                displayBlock($(Main_location_photo));
                            }
                        } catch (final Throwable t) {
                            EXCEPTION.error("{}", t);

                        }
                    }
                }

                final Return<Location> r;
                if (WOEID != null) {
                    r = DB.getHumanCRUDLocationLocal(true).dirtyRLocation(WOEID);
                } else {
                    r = new ReturnImpl<Location>(ExceptionCache.UNSUPPORTED_OPERATION_EXCEPTION, "Search Unavailable", false);
                    //r = DB.getHumanCRUDLocationLocal(true).dirtyRLocation(location, superLocation);
                }


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
                            final Element hiddenLocationIdInputTag = $(INPUT);
                            hiddenLocationIdInputTag.setAttribute(INPUT.type(), INPUT.typeValueHidden());
                            hiddenLocationIdInputTag.setAttribute(INPUT.id(), JSCodeToSend.LocationId);
                            hiddenLocationIdInputTag.setAttribute(INPUT.value(), existingLocation_.getLocationId().toString());
                            hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }

                    setLocationNameForJSReference:
                    {
                        try {
                            final Element hiddenLocationIdInputTag = $(INPUT);
                            hiddenLocationIdInputTag.setAttribute(INPUT.type(), INPUT.typeValueHidden());
                            hiddenLocationIdInputTag.setAttribute(INPUT.id(), JSCodeToSend.LocationName);
                            hiddenLocationIdInputTag.setAttribute(INPUT.value(), existingLocation_.getLocationName() + " of " + existingLocation_.getLocationSuperSet().getLocationName());
                            hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }


                    setLocationAsPageTopic:
                    {
                        try {
                            $(Main_center_main_location_title).setTextContent("This is " + existingLocation_.getLocationName() + " of " + existingLocation_.getLocationSuperSet());

                            $(Main_location_backlink).appendChild($(P).appendChild(hTMLDocument__.createTextNode("Back to" + COLON + " ")));
                            $(Main_location_backlink).appendChild(generateSimpleLocationLink(existingLocation_.getLocationSuperSet()));

                            $(Main_location_list_header).appendChild(($(P).appendChild(hTMLDocument__.createTextNode("At " + existingLocation_.getLocationName() + " you can visit" + COLON + " "))));

                            for (final Element element : generateLocationLinks(DB.getHumanCRUDLocationLocal(true).doDirtyRLocationsBySuperLocation(existingLocation_))) {
                                $(Main_location_list).appendChild(element);
                                displayBlock($(Main_notice_sh));
                            }

                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }

                    }

                    setFlickrLink:
                    {
//                        $(Main_flickr).setAttribute(MarkupTag.A.href(), RBGet.config.getString("flickr.json") + location.toLowerCase().replace(" ", "+").replace("/", "+"));
                    }

                    getAndDisplayAllThePhotos:
                    {
                        List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
                        sl.appendToLogMSG(NUMBER_OF_PHOTOS_FOR + existingLocation_.getLocationName() + COLON + listPublicPhoto.size());

                        int i = 0;
                        for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {
                            try {
                                final PublicPhoto publicPhoto = it.next();

                                //old mode pasted end of class if needed
                                newMode:
                                {
                                    final Element image = $(IMG);
                                    image.setAttribute(IMG.src(), publicPhoto.getPublicPhotoURLPath());
                                    image.setAttribute(IMG.alt(), publicPhoto.getPublicPhotoDescription());
                                    image.setAttribute(IMG.style(), "width" + COLON + "110px;");

                                    final Element link = $(A);
                                    link.setAttribute(A.href(), publicPhoto.getPublicPhotoURLPath());

                                    link.appendChild(image);

                                    $(Main_yox).appendChild(link);
                                }
                            } catch (final Throwable t) {
                                Loggers.EXCEPTION.error("", t);
                            }
                        }
                    }

                } else {
                    noSupportForNewLocations:
                    {
                        try {
//                            $(Main_notice).appendChild(($(P).appendChild(
//                                    hTMLDocument__.createTextNode(RBGet.logMsgs.getString("CANT_FIND_LOCATION")
//                                            + " Were you looking for "
//                                    ))));
//                            for (final Element element : generateLocationLinks(DB.getHumanCRUDLocationLocal(true).dirtyRLikeLocations(location))) {
//                                $(Main_notice).appendChild(element);
//                                displayBlock($(Main_notice_sh));
//                            }
                            NotSupportingLikeSearchTooForNow:
                            {
                                $(Main_notice).appendChild(($(P).appendChild(hTMLDocument__.createTextNode(RBGet.logMsgs.getString("CANT_FIND_LOCATION")))));
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("", t);
                        }
                    }
                }
                sl.complete(Loggers.LEVEL.SERVER_STATUS, Loggers.DONE);
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

            private List<Element> generateLocationLinks(final List<Location> locationList) {

                final ElementComposer UList = ElementComposer.compose($(UL)).$ElementSetAttribute(MarkupTag.UL.id(), "place_list");

                for (Location location : locationList) {
                    final Element link = $(A);

                    link.setTextContent("Travel to " + location.getLocationName() + " of " + location.getLocationSuperSet().getLocationName());

                    link.setAttribute(A.href(),
                            "/page/"
                                    + location.getLocationName()
                                    + "_of_"
                                    + location.getLocationSuperSet().getLocationName()
                                    + Parameter.get(Location.WOEID, location.getWOEID().toString(), true));

                    link.setAttribute(A.alt(),
                            "/page/" + location.getLocationName() + "_of_" + location.getLocationSuperSet().getLocationName());

                    link.setAttribute(A.title(),
                            "Click to explore " + location.getLocationName() + " of " + location.getLocationSuperSet().getLocationName());

                    link.setAttribute(A.classs(), "vtip");


                    final Element linkDiv = $(DIV);


                    linkDiv.appendChild(link);

                    UList.wrapThis(
                            ElementComposer.compose($(LI)).wrapThis(linkDiv).get()
                    );
                }

                final List<Element> elements = new ArrayList<Element>();
                elements.add(UList.get());
                return elements;

            }
//            private List<Element> generateLocationLinks
//                    (
//                            final List<Location> locationList) {
//
//                List<Element> links = new ArrayList<Element>();
//
//                for (Location location : locationList) {
//                    final Element link = $(A);
//
//                    link.setTextContent("Travel to " + location.getLocationName() + " of " + location.getLocationSuperSet().getLocationName());
//
//                    link.setAttribute(A.href(),
//                            "/page/"
//                                    + location.getLocationName()
//                                    + "_of_"
//                                    + location.getLocationSuperSet().getLocationName()
//                                    + Parameter.get(Location.WOEID, location.getWOEID().toString(), true));
//
//                    link.setAttribute(A.alt(),
//                            "/page/" + location.getLocationName() + "_of_" + location.getLocationSuperSet().getLocationName());
//
//                    link.setAttribute(A.title(),
//                            "Click to explore " + location.getLocationName() + " of " + location.getLocationSuperSet().getLocationName());
//
//                    link.setAttribute(A.classs(), "vtip");
//
//
//                    final Element linkDiv = $(DIV);
//
//                    linkDiv.appendChild(link);
//
//                    links.add(linkDiv);
//                }
//
//                return links;
//
//            }

            private Element generateLocationLink(final Location location) {
                final Element link = $(A);
                link.setTextContent("Travel to " + location.getLocationName() + " of " + location.getLocationSuperSet().getLocationName());
                link.setAttribute(A.href(),
                        "/page/"
                                + location.getLocationName()
                                + "_of_"
                                + location.getLocationSuperSet().getLocationName()
                                + Parameter.get(Location.WOEID, location.getWOEID().toString(), true));

                link.setAttribute(A.alt(),
                        "/page/" + location.getLocationName() + "_of_" + location.getLocationSuperSet().getLocationName());
                return link;
            }

            private Element generateSimpleLocationLink(final Location location) {
                final Element link = $(A);
                link.setTextContent(location.getLocationName());
                link.setAttribute(A.href(),
                        "/page/"
                                + location.getLocationName()
                                + Parameter.get(Location.WOEID, location.getWOEID().toString(), true));

                link.setAttribute(A.alt(),
                        "/page/" + location.getLocationName());
                return link;
            }
        };//Listener
    }
}


//                                oldMode:
//                                {
//                                    if (i % 2 == 0) {
//                                        new Photo$Description(itsNatDocument__, $(Main_center_main)) {
//                                            @Override
//                                            protected void init(final Object... initArgs) {
//                                                $$(pd_photo_permalink).setAttribute(A.href(), publicPhoto.getPublicPhotoURLPath() + "|" + publicPhoto.getPublicPhotoName());
//                                                $$(pd_photo).setAttribute(A.src(), publicPhoto.getPublicPhotoURLPath());
//                                                $$(pd_photo_description).setTextContent(publicPhoto.getPublicPhotoDescription());
//                                            }
//                                        };
//                                    } else {
//                                        new Photo$Description(itsNatDocument__, $(Main_center_main)) {
//                                            @Override
//                                            protected void init(final Object... initArgs) {
//                                                $$(pd_photo_permalink).setAttribute(A.href(), publicPhoto.getPublicPhotoURLPath() + "|" + publicPhoto.getPublicPhotoName());
//                                                $$(pd_photo).setAttribute(A.src(), publicPhoto.getPublicPhotoURLPath());
//                                                $$(pd_photo_description).setTextContent(publicPhoto.getPublicPhotoDescription());
//
//                                            }
//                                        };
//                                    }
//                                }
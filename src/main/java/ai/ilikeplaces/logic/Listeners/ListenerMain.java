package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.TODO;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.logic.Listeners.widgets.Photo$Description;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractListener;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * @author Ravindranath Akila
 */
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
            @TODO(task = "IF LOCATION IS NOT AVAILABLE, IT SHOULD BE ADDED THROUGH A WIDGET(OR FRAGMENT MAYBE?)")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor + JSCodeToSend.FnLocationId + JSCodeToSend.FnLocationName + JSCodeToSend.FnSetTitle);
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");
                setMainTitle:
                {
                    try {
                        $(mainTitle).setTextContent("Welcome to ilikeplaces!");
                    } catch (final Exception e__) {
                        logger.debug(e__.getMessage());
                    }
                }
                signOnActions:
                {
                    if (getUsername() != null) {

                        setUsersDisplayNameIfLoggedIn:
                        {
                            final Element usersName = $(MarkupTag.P);
                            usersName.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsername());
                            $(Main_othersidebar_identity).appendChild(usersName);
                        }
                        setProfileLink:
                        {
                            $(Main_othersidebar_profile_link).setAttribute("href", RBGet.config.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0002"));
                        }
                    } else {
                        setSignupLink:
                        {
                            $(Main_othersidebar_profile_link).setAttribute("href", RBGet.config.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0003"));
                        }
                        setDisplayNameAlternative:
                        {
                            final Element location = $(MarkupTag.P);
                            location.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0005") + location);
                            $(Main_othersidebar_identity).appendChild(location);
                        }
                    }
                }
                final Location loc_ = DB.getHumanCRUDLocationLocal(true).doDirtyHumanRLocation(location, null);

                if (loc_ != null) {
                    final Location existingLocation_ = loc_;

                    showUploadFileLink:
                    {
                        if (getUsername() != null) {
                            $(Main_othersidebar_upload_file_sh).setAttribute("style", "display:block;");
                        }
                    }

                    setLocationIdForJSReference:
                    {
                        final Element hiddenLocationIdInputTag = $(MarkupTag.INPUT);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.type(), MarkupTag.INPUT.typeValueHidden());
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.id(), JSCodeToSend.LocationId);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.value(), existingLocation_.getLocationId().toString());
                        hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                    }

                    setLocationNameForJSReference:
                    {
                        @TODO(task = "MOVE TO GUI MSGS I18N METHODICALLY")
                        final Element hiddenLocationIdInputTag = $(MarkupTag.INPUT);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.type(), MarkupTag.INPUT.typeValueHidden());
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.id(), JSCodeToSend.LocationName);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.value(),
                                "This is " + existingLocation_.getLocationName() + " of " + existingLocation_.getLocationSuperSet().getLocationName() + ". " +
                                        "You are at " + existingLocation_.getLocationName() + " in " + existingLocation_.getLocationSuperSet().getLocationName() + ". ");
                        hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                    }

                    setLocationAsTitle:
                    {
                        @TODO(task = "CONTACT JOSE MARIA REGARDING THE EXCEPTION OF TAG NOT BEING AVAILABLE WHEN SETTITLE IS USED")
                        final String pageTitle = existingLocation_.getLocationName();
                    }

                    setLocationAsPageTopic:
                    {
                        $(Main_center_main_location_title).setTextContent("This is " + existingLocation_.getLocationName() + " of " + existingLocation_.getLocationSuperSet());
                    }


                    getAndDisplayAllThePhotos:
                    {
                        List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
                        logger.info(RBGet.logMsgs.getString("NUMBER_OF_PHOTOS_FOR_LOCATION"), existingLocation_.getLocationName(), listPublicPhoto.size());

                        @TODO(task = "INVERT COLORS AND SET PHOTO NAME")
                        int i = 0;
                        for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {
                            final PublicPhoto publicPhoto = it.next();
                            if (i % 2 == 0) {
                                new Photo$Description(itsNatDocument__, $(Main_center_main)) {
                                    @Override
                                    protected void init() {
                                        $$(pd_photo_permalink).setAttribute("href", publicPhoto.getPublicPhotoURLPath() + "|" + publicPhoto.getPublicPhotoName());
                                        $$(pd_photo).setAttribute("src", publicPhoto.getPublicPhotoURLPath());
                                        final Element descriptionText = $$(MarkupTag.P);
                                        descriptionText.setTextContent(publicPhoto.getPublicPhotoDescription());
                                        $$(pd_photo_description).appendChild(descriptionText);
                                    }
                                };
                            } else {
                                new Photo$Description(itsNatDocument__, $(Main_center_main)) {
                                    @Override
                                    protected void init() {
                                        $$(pd_photo_permalink).setAttribute("href", publicPhoto.getPublicPhotoURLPath() + "|" + publicPhoto.getPublicPhotoName());
                                        $$(pd_photo).setAttribute("src", publicPhoto.getPublicPhotoURLPath());
                                        final Element descriptionText = $$(MarkupTag.P);
                                        descriptionText.setTextContent(publicPhoto.getPublicPhotoDescription());
                                        $$(pd_photo_description).appendChild(descriptionText);

                                    }
                                };
                            }
                        }
                    }

                } else {
                    noSupportForNewLocationsYet:
                    {
                        $(Main_notice).appendChild(($(MarkupTag.P).appendChild(
                                hTMLDocument__.createTextNode(RBGet.logMsgs.getString("CANT_FIND_LOCATION")
                                        + " Were you looking for " + Arrays.toString(DB.getHumanCRUDLocationLocal(true).doDirtyHumanRLikeLocationName(location).toArray())
                                ))));
                        $(Main_notice_sh).setAttribute("style", "display:block;");
                        logger.debug(RBGet.logMsgs.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0001"), DB.getHumanCRUDLocationLocal(true).doDirtyHumanRLikeLocationName(location));
                    }
                }

            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
/*
                *//*Abstract implementation*//*
                itsNatHTMLDocument__.addEventListener((EventTarget) $("cool"), Click, new EventListener() {

                    @Override
                    public void handleEvent(final Event evt_) {
                        $(Main_sidebar).appendChild(hTMLDocument__.createTextNode(sessionBoundBadRefWrapper != null ? sessionBoundBadRefWrapper.boundInstance.getHumanUserId() : "Logged Out!"));
                        $(Main_sidebar).appendChild(hTMLDocument__.createTextNode("COOL! "));
                        new Photo$Description(itsNatDocument__, $(Main_left_column)) {
                        };
                    }
                }, false);

                *//*Abstract implementation*//*
                itsNatHTMLDocument__.addEventListener((EventTarget) $("hot"), Click, new EventListener() {

                    @Override
                    public void handleEvent(final Event evt_) {
                        $(Main_sidebar).appendChild(hTMLDocument__.createTextNode("HOT! "));
                        new Photo$Description(itsNatDocument, $(Main_right_column)) {
                        };
                    }
                }, false);
                */
            }
        };
    }

    /**
     * @param showChangeLog__
     * @return changeLog
     */
    public String toString(final boolean showChangeLog__) {
        String changeLog = new String(toString() + "\n");
        changeLog += "20090918 crudServiceLocal was throwing a bug(exception). Should be due to garbage colection of outer class."
                + "Moved it to init. Did not pursue reason as now implementation is sane and previous was not.\n";
        changeLog += "20090924 crudServiceLocal was shifter back to the original position with validation. Not a bug. "
                + "Outer class has a reference from the inner class so never gets garbage collected.";
        return showChangeLog__ ? changeLog : toString();
    }
}

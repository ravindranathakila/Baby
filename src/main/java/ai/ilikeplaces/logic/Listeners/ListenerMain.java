package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.doc.*;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Element;
import ai.ilikeplaces.logic.crud.DB;
import static ai.ilikeplaces.servlets.Controller.Page.*;
import java.util.Iterator;
import java.util.List;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author Ravindranath Akila
 */
public class ListenerMain implements ItsNatServletRequestListener {


    final static private Logger logger = LoggerFactory.getLogger(ListenerMain.class);
    @TODO(task="Move to several budnles as i18n,EXCEPTION,LOG where they can be used from several jars. Use a package structure like ai.ilikeplaces.rb.i18n")
    private static final ResourceBundle logMsgs = ResourceBundle.getBundle("LogMsgs");
    final static protected String LocationId = logMsgs.getString("LOCATIONID");

    /**
     *
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

                final Location loc_ = DB.getHumanCRUDLocationLocal(true).doDirtyHumanRLocation(location, null);

                if (loc_ != null) {
                    final Location existingLocation_ = loc_;

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
                        final Element hiddenLocationIdInputTag = $(MarkupTag.INPUT);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.type(), MarkupTag.INPUT.typeValueHidden());
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.id(), JSCodeToSend.LocationName);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.value(), existingLocation_.getLocationName());
                        hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                    }

                    setLocationAsTitle:
                    {
                        try{
                        hTMLDocument__.setTitle("Test title");
                        } catch (Throwable t){
                            logger.error("{}",t);
                        }
                    }
                    
                    getAndDisplayAllThePhotos:
                    {
                        List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
                        logger.info(logMsgs.getString("NUMBER_OF_PHOTOS_FOR_LOCATION"), existingLocation_.getLocationName(), listPublicPhoto.size());

                        int i = 0;
                        for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {
                            final PublicPhoto publicPhoto = it.next();
                            if (i % 2 == 0) {
                                new Photo$Description(itsNatDocument__, $(Main_left_column)) {

                                    @Override
                                    protected void init() {
                                        $$(pd_photo_permalink).setAttribute("href", publicPhoto.getPublicPhotoURLPath() + "|" + "PHOTO TITLE");
                                        $$(pd_photo).setAttribute("src", publicPhoto.getPublicPhotoURLPath());
                                        final Element descriptionText = $$(MarkupTag.P);
                                        descriptionText.setTextContent(publicPhoto.getPublicPhotoDescription());
                                        $$(pd_photo_description).appendChild(descriptionText);

                                    }
                                };
                            } else {
                                new Photo$Description(itsNatDocument__, $(Main_right_column)) {

                                    @Override
                                    protected void init() {
                                        $$(pd_photo_permalink).setAttribute("href", publicPhoto.getPublicPhotoURLPath() + "|" + "PHOTO TITLE");
                                        $$(pd_photo).setAttribute("src", publicPhoto.getPublicPhotoURLPath());
                                    }
                                };
                            }
                        }
                    }

                } else {
                    noSupportForNewLocationsYet:
                    {
                        $(Main_center_main).appendChild(($(MarkupTag.P).appendChild(
                                hTMLDocument__.createTextNode(logMsgs.getString("CANT_FIND_LOCATION")))));
                    }
                }

            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
//                /*Abstract implementation*/
//                itsNatHTMLDocument__.addEventListener((EventTarget) $("cool"), Click, new EventListener() {
//
//                    @Override
//                    public void handleEvent(final Event evt_) {
//                        $(Main_sidebar).appendChild(hTMLDocument__.createTextNode(sessionBoundBadReferenceWrapper != null ? sessionBoundBadReferenceWrapper.boundInstance.getHumanUserId() : "Logged Out!"));
//                        $(Main_sidebar).appendChild(hTMLDocument__.createTextNode("COOL! "));
//                        new Photo$Description(itsNatDocument__, $(Main_left_column)) {
//                        };
//                    }
//                }, false);
//
//                /*Abstract implementation*/
//                itsNatHTMLDocument__.addEventListener((EventTarget) $("hot"), Click, new EventListener() {
//
//                    @Override
//                    public void handleEvent(final Event evt_) {
//                        $(Main_sidebar).appendChild(hTMLDocument__.createTextNode("HOT! "));
//                        new Photo$Description(itsNatDocument, $(Main_right_column)) {
//                        };
//                    }
//                }, false);
            }
        };
    }

    /**
     *
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

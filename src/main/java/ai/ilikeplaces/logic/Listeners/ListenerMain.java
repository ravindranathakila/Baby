package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.logic.crud.unit.RLocationLocal;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.logic.crud.*;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.NamingException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.Element;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ch.qos.logback.classic.LoggerContext;
import static ai.ilikeplaces.servlets.Controller.Page.*;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;


import ch.qos.logback.core.util.StatusPrinter;

/**
 *
 * @author Ravindranath Akila
 */
public class ListenerMain implements ItsNatServletRequestListener {

    final static protected String LocationId = "locationId";
    final protected static String JsCodeToSend = "document.monitor = new EventMonitor(); \n" +
            "document.getItsNatDoc().addEventMonitor(document.monitor); \n" +
            "/*Function for gettling LocationId from hidden variable*/\n" +
            "function getLocationId(){return document.getElementById('" + LocationId + "').value;}\n";
    private CrudServiceLocal<Location> crudServiceLocal = null;
    private RLocationLocal rLocationLocal_;
    final private Logger logger = LoggerFactory.getLogger(ListenerMain.class);

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public ListenerMain() {
        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            try {
                final Properties p_ = new Properties();
                p_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
                final Context context = new InitialContext(p_);

                crudServiceLocal = (CrudServiceLocal<Location>) context.lookup("CrudServiceLocal");

                rLocationLocal_ = (RLocationLocal) context.lookup("RLocationLocal");

            } catch (NamingException ex) {
                log.append("SORRY! COULD NOT INITIALIZE " + getClass().getName() + " SERVLET DUE TO A NAMING EXCEPTION!");
                logger.error("SORRY! COULD NOT INITIALIZE " + getClass().getName() + " DUE TO A NAMING EXCEPTION!", ex);
                break init;
            }

            /**
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ExceptionConstructorInvokation(log.toString());
        }
    }

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
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JsCodeToSend);


                final Location loc_ = rLocationLocal_.doRLocation(location, null);

                if (loc_ != null) {
                    final Location existingLocation_ = loc_;

                    setLocationIdForJSReference:
                    {
                        final Element hiddenLocationIdInputTag = $(MarkupTag.INPUT);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.type(), MarkupTag.INPUT.typeValueHidden());
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.id(), LocationId);
                        hiddenLocationIdInputTag.setAttribute(MarkupTag.INPUT.value(), existingLocation_.getLocationId().toString());
                        hTMLDocument__.getBody().appendChild(hiddenLocationIdInputTag);
                    }

                    $("Main_center_main").appendChild(
                            $(MarkupTag.P).appendChild(
                            hTMLDocument__.createTextNode("\nINFO:" +
                            existingLocation_.getLocationInfo() +
                            "\nSUPERSET:" +
                            existingLocation_.getLocationSuperSet() +
                            "\nTOSTRING:" +
                            existingLocation_.toString(true))));
                    List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
                    logger.info("Hello, number of photos:{}", listPublicPhoto.size());


                    int i = 0;
                    for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {
                        final PublicPhoto publicPhoto = it.next();
                        if (i % 2 == 0) {
                            new Photo$Description(itsNatDocument__, $("Main_left_column")) {

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
                            new Photo$Description(itsNatDocument__, $("Main_right_column")) {

                                @Override
                                protected void init() {
                                    $$("pd_photo_permalink").setAttribute("href", publicPhoto.getPublicPhotoURLPath() + "|" + "PHOTO TITLE");
                                    $$("pd_photo").setAttribute("src", publicPhoto.getPublicPhotoURLPath());
                                }
                            };
                        }
                    }

                } else {
                    $("Main_center_main").appendChild(($(MarkupTag.P).appendChild(
                            hTMLDocument__.createTextNode("Create Location"))));
                    //crudServiceLocal.create(loc_);
                }

            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                /*Abstract implementation*/
                itsNatHTMLDocument__.addEventListener((EventTarget) $("cool"), Click, new EventListener() {

                    @Override
                    public void handleEvent(final Event evt_) {
                        $("Main_sidebar").appendChild(hTMLDocument__.createTextNode(sessionBoundBadReferenceWrapper != null ? sessionBoundBadReferenceWrapper.boundInstance.getLoggedOnUserId() : "Logged Out!"));
                        $("Main_sidebar").appendChild(hTMLDocument__.createTextNode("COOL! "));
                        new Photo$Description(itsNatDocument__, $("Main_left_column")) {
                        };
                    }
                }, false);

                /*Abstract implementation*/
                itsNatHTMLDocument__.addEventListener((EventTarget) $("hot"), Click, new EventListener() {

                    @Override
                    public void handleEvent(final Event evt_) {
                        $("Main_sidebar").appendChild(hTMLDocument__.createTextNode("HOT! "));
                        new Photo$Description(itsNatDocument, $("Main_right_column")) {
                        };
                    }
                }, false);
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
        changeLog += "20090918 crudServiceLocal was throwing a bug(exception). Should be due to garbage colection of outer class." +
                "Moved it to init. Did not pursue reason as now implementation is sane and previous was not.\n";
        changeLog += "20090924 crudServiceLocal was shifter back to the original position with validation. Not a bug. " +
                "Outer class has a reference from the inner class so never gets garbage collected.";
        return showChangeLog__ ? changeLog : toString();
    }
}

package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.widgets.Photo$Description;
import ai.ilikeplaces.widgets.AbstractWidgetListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.Element;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.html.HTMLDocument;

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
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<Location> crudServiceLocal = null;
    final private Logger logger = Logger.getLogger(ListenerMain.class.getName());

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
                p_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
                context = new InitialContext(p_);
                if (context == null) {
                    log.append("\nVARIABLE context IS NULL! ");
                    log.append(context);
                    break init;
                }

                crudServiceLocal = (CrudServiceLocal<Location>) context.lookup("CrudServiceLocal");
                logger.info("LISTNER MAIN CRUD SERVICE:" + crudServiceLocal.hashCode());
                if (crudServiceLocal == null) {
                    log.append("\nVARIABLE crudServiceLocal_ IS NULL! ");
                    log.append(crudServiceLocal);
                    break init;

                }
            } catch (NamingException ex) {
                log.append("\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " SERVLET DUE TO A NAMING EXCEPTION!");
                logger.log(Level.SEVERE, "\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " DUE TO A NAMING EXCEPTION!", ex);
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

                final String locationName_ = location;

                final Location loc_ = new Location();

                loc_.setLocationName(locationName_);

                final List<Location> existingLocations = crudServiceLocal.findWithNamedQuery(Location.FindAllLocationsByName,
                        QueryParameter.with("locationName", loc_.getLocationName()).parameters());

                final int resultSetSize = existingLocations.size();

                if (resultSetSize == 1) {
                    final Location existingLocation_ = existingLocations.get(0);

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
                            hTMLDocument__.createTextNode(
                            existingLocation_.getLocationInfo() +
                            ":" +
                            existingLocation_.getLocationSuperSet() +
                            ":" +
                            existingLocation_.toString(true))));
                    List<PublicPhoto> listPublicPhoto = existingLocation_.getPublicPhotos();
                    logger.info("Hello, number of photos:" + String.valueOf(listPublicPhoto.size()));

                    int i = 0;
                    for (final Iterator<PublicPhoto> it = listPublicPhoto.iterator(); it.hasNext(); i++) {
                        final PublicPhoto publicPhoto = it.next();
                        if (i % 2 == 0) {
                            new Photo$Description(itsNatDocument__, $("Main_left_column")) {

                                @Override
                                protected void init() {
                                    $$("pd_photo_permalink").setAttribute("href", publicPhoto.getPublicPhotoURLPath() + "|" + "PHOTO TITLE");
                                    $$("pd_photo").setAttribute("src", publicPhoto.getPublicPhotoURLPath());
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

                } else if (resultSetSize == 0) {
                    $("Main_center_main").appendChild(($(MarkupTag.P).appendChild(
                            hTMLDocument__.createTextNode("<a href=\"#\" onClick=\"javascript:alert('clicked');\">Click here to create</a>"))));
                    //crudServiceLocal.create(loc_);
                } else if (resultSetSize > 1) {
                    $("Main_center_main").appendChild(($(MarkupTag.P).appendChild(hTMLDocument__.createTextNode("Many entries!"))));
                } else {
                    throw new IllegalStateException("SORRY! I HAVE A LOCALTION RESLULTSET < 0 WHICH IS NOT POSSIBLE!");
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

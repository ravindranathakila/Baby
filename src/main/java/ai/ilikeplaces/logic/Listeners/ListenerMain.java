package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.*;
import ai.ilikeplaces.logic.Listeners.AbstractListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import ai.ilikeplaces.widgets.*;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
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

    /**
     *
     */
    final protected static String JsCodeToSend = "document.monitor = new EventMonitor(); \n" + "document.getItsNatDoc().addEventMonitor(document.monitor); \n";
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

                crudServiceLocal = (CrudServiceLocal) context.lookup("CrudServiceLocal");
                logger.info("LISTNER MAIN CRUD SERVICE:"+crudServiceLocal.hashCode());
                if (crudServiceLocal == null) {
                    log.append("\nVARIABLE crudServiceLocal_ IS NULL! ");
                    log.append(crudServiceLocal);
                    break init;

                }
            } catch (NamingException ex) {
                log.append("\nSORRY! COULD NOT INITIALIZE "+getClass().getName()+" SERVLET DUE TO A NAMING EXCEPTION!");
                logger.log(Level.SEVERE, "\nSORRY! COULD NOT INITIALIZE "+getClass().getName()+" DUE TO A NAMING EXCEPTION!", ex);
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
            protected final void init() {
                itsNatDocument_.addCodeToSend(JsCodeToSend);
                if (location_ != null) {
                    final String locationName_ = location_;
                    final String locationInfo_ = location_ + " is a now registered on www.ilikeplaces.com." + System.currentTimeMillis();

                    final Location loc_ = new Location();

                    loc_.setLocationName(locationName_);
                    loc_.setLocationInfo(locationInfo_);

                    final List<Location> existingLocations = crudServiceLocal.findWithNamedQuery(Location.FindAllLocationsByName,
                            QueryParameter.with("locationName", loc_.getLocationName()).parameters());

                    Location existingLocation = existingLocations.size() == 1 ? existingLocations.get(0) : null;

                    if (existingLocation == null) {
                        crudServiceLocal.create(loc_);
                        getElementById("Main_temp1").appendChild(itsNatDocument_.getDocument().createTextNode("Create an entry for this location"));

                    } else {
                        //Kandy 1252410471093 Sigiriya 1252410457640
                        //Location result = (Location) manager.createQuery("SELECT location FROM Location location WHERE location.locationName = :locationName").setParameter("locationName", location_).getSingleResult();

                        existingLocation = existingLocations.size() == 1 ? existingLocations.get(0) : null;

                        getElementById("Main_temp1").appendChild(itsNatDocument_.getDocument().createTextNode(existingLocation.getLocationInfo()+":"+existingLocation.getLocationSuperSet()+":"+existingLocation.toString(true)));
                    }

                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                /*Abstract implementation*/
                itsNatHTMLDocument__.addEventListener((EventTarget) getElementById("Main_temp1"), Click, new EventListener() {

                    @Override
                    public void handleEvent(final Event evt_) {
                        getElementById("Main_sidebar").appendChild(hTMLDocument__.createTextNode(sBLoggedOnUserFace == null ? "NULL" : sBLoggedOnUserFace.getLoggedOnUserId()));
                        getElementById("Main_sidebar").appendChild(hTMLDocument__.createTextNode("COOL! "));
                        new Photo$Description(itsNatDocument__, getElementById("Main_temp1")) {
                        };
                    }
                }, false);

                /*Abstract implementation*/
                itsNatHTMLDocument_.addEventListener((EventTarget) getElementById("Main_temp2"), Click, new EventListener() {

                    @Override
                    public void handleEvent(final Event evt_) {
                        getElementById("Main_sidebar").appendChild(itsNatDocument_.getDocument().createTextNode("HOT! "));
                        new Photo$Description(itsNatDocument_, getElementById("Main_temp2")) {
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

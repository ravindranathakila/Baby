package ai.ilikeplaces;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.http.ItsNatHttpSession;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import ai.ilikeplaces.widgets.*;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.servlets.ServletLogin;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author Ravindranath Akila
 */
public class ListenerMain implements ItsNatServletRequestListener {

    private CrudServiceLocal<Location> crudServiceLocal;
    final protected static String JsCodeToSend = "document.monitor = new EventMonitor(); \n" + "document.getItsNatDoc().addEventMonitor(document.monitor); \n";

    @SuppressWarnings("unchecked")
    public ListenerMain() {
        try {
            final Properties p = new Properties();
            p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
            final Context context = new InitialContext(p);
            crudServiceLocal = (CrudServiceLocal) context.lookup("CrudServiceLocal");
        } catch (NamingException ex) {
            Logger.getLogger(ListenerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            protected void registerEventListeners() {
                /*Abstract implementation*/
                itsNatHTMLDocument_.addEventListener((EventTarget) getElementById("Main_temp1"), Click, new EventListener() {

                    @Override
                    public void handleEvent(final Event evt_) {
                        final String userId = (String) itsNatHttpSession.getAttribute(ServletLogin.User);
                        getElementById("Main_sidebar").appendChild(itsNatDocument_.getDocument().createTextNode(userId == null ? "NULL" : userId));
                        getElementById("Main_sidebar").appendChild(itsNatDocument_.getDocument().createTextNode("COOL! "));
                        new Photo$Description(itsNatDocument_, getElementById("Main_temp1")) {
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
}

package ai.ilikeplaces;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import ai.ilikeplaces.widgets.*;
import ai.ilikeplaces.entities.*;

/**
 *
 * @author Ravindranath Akila
 */
public class ListenerMain implements ItsNatServletRequestListener {

    public void processRequest(final ItsNatServletRequest request_, final ItsNatServletResponse response_) {
        new AbstractListener(request_) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            protected final void init() {
                String code = "";
                code += "document.monitor = new EventMonitor(); \n";
                code += "document.getItsNatDoc().addEventMonitor(document.monitor); \n";
                itsNatDocument_.addCodeToSend(code);
                if(location_ != null){
                    EntityManagerFactory factory = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU");
                    EntityManager manager = factory.createEntityManager();
                    String locationName_ = "Sigiriya";
                    String locationInfo_ = "Sigiriya is a world heritage in located Sri Lanka.";

                    Location loc = new Location();

                    loc.setLocationName(locationName_);
                    loc.setLocationInfo(locationInfo_);

                    manager.getTransaction().begin();
                    manager.persist(loc);
                    manager.getTransaction().commit();
                    
                    Location result = (Location) manager.createQuery("SELECT location FROM Location location WHERE location.locationName = :locationName").setParameter("locationName", location_).getSingleResult();

                    manager.close();
                    factory.close();
                    getElementById("Main_temp1").appendChild(itsNatDocument_.getDocument().createTextNode(result.getLocationInfo()));
                } else {
                    getElementById("Main_temp1").appendChild(itsNatDocument_.getDocument().createTextNode("Create an entry for this location"));
                }

            }

            private void Null(Object obj) {
                if (obj == null) {
                    throw new RuntimeException("NULL");
                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            protected void registerEventListeners() {
                /*Abstract implementation*/
                itsNatHTMLDocument_.addEventListener((EventTarget) getElementById("Main_temp1"), Click, new EventListener() {

                    public void handleEvent(final Event evt_) {
                        getElementById("Main_sidebar").appendChild(itsNatDocument_.getDocument().createTextNode("COOL! "));
                        new Photo$Description(itsNatDocument_, getElementById("Main_temp1")) {
                        };
                    }
                }, false);

                /*Abstract implementation*/
                itsNatHTMLDocument_.addEventListener((EventTarget) getElementById("Main_temp2"), Click, new EventListener() {

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

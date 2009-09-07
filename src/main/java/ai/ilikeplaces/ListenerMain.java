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
import ai.ilikeplaces.ListenerMainLocal;
import java.util.Properties;
import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author Ravindranath Akila
 */
public class ListenerMain implements ItsNatServletRequestListener, ListenerMainLocal {

    private Context context;
//    @PersistenceContext(unitName = "adimpression_ilikeplaces_war_1.6-SNAPSHOTPU", type = PersistenceContextType.TRANSACTION)
//    private EntityManager manager;
    private CrudServiceLocal<Location> crudServiceLocal;

    public ListenerMain() {
        try {
            Properties p = new Properties();
            p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
            context = new InitialContext(p);
            crudServiceLocal = ((TempLocal) context.lookup("TempLocal")).temp();
        } catch (Throwable t) {
            t.printStackTrace(System.out);
        }
    }

    public boolean is() {
        throw new java.lang.UnsupportedOperationException("Not defined yet");
    }

    public boolean is2() {
        return crudServiceLocal == null;
    }

    public void processRequest(final ItsNatServletRequest request_, final ItsNatServletResponse response_) {
        new AbstractListener(request_) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init() {
//                if(crudServiceLocal == null){
//                    int a = 0;
//                    int b = 10/a;
//                }
                String code = "";
                code += "document.monitor = new EventMonitor(); \n";
                code += "document.getItsNatDoc().addEventMonitor(document.monitor); \n";
                itsNatDocument_.addCodeToSend(code);
                if (location_ != null) {
//                    CrudService<Location> service = new CrudService<Location>();

//                    EntityManagerFactory factory = Persistence.createEntityManagerFactory("adimpression_ilikeplaces_war_1.6-SNAPSHOTPU");
//                    EntityManager manager = factory.createEntityManager();
                    final String locationName_ = "Sigiriya";
                    final String locationInfo_ = "Sigiriya is a world heritage in located Sri Lanka." + System.currentTimeMillis();

                    final Location loc = new Location();

                    loc.setLocationName(locationName_);
                    loc.setLocationInfo(locationInfo_);

                    crudServiceLocal.update(loc);
//                    manager.getTransaction().begin();
//                    manager.persist(loc);
//                    manager.getTransaction().commit();

//                    Location result = (Location) manager.createQuery("SELECT location FROM Location location WHERE location.locationName = :locationName").setParameter("locationName", location_).getSingleResult();
                    Location result = (Location) crudServiceLocal.find(Location.class, "Sigiriya");

//                    manager.close();
//                    factory.close();
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

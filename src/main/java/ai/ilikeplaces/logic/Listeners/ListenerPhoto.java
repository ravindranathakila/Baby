package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.jpa.QueryParameter;
import ai.ilikeplaces.widgets.Photo$Description;
import ai.ilikeplaces.widgets.AbstractWidgetListener;
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
import ai.ilikeplaces.servlets.Controller;
import static ai.ilikeplaces.servlets.Controller.Page.*;
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
public class ListenerPhoto implements ItsNatServletRequestListener {

    final static protected String LocationId = "locationId";
    final protected static String JsCodeToSend = "document.monitor = new EventMonitor(); \n" +
            "document.getItsNatDoc().addEventMonitor(document.monitor); \n";
    final private String permaLink = null;
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<Location> crudServiceLocal = null;
    final private Logger logger = LoggerFactory.getLogger(ListenerPhoto.class.getName());

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public ListenerPhoto() {
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
                logger.info(""+crudServiceLocal.hashCode());
                if (crudServiceLocal == null) {
                    log.append("\nVARIABLE crudServiceLocal_ IS NULL! ");
                    log.append(crudServiceLocal);
                    break init;

                }
            } catch (NamingException ex) {
                log.append("\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " SERVLET DUE TO A NAMING EXCEPTION!");
                logger.info( "\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " DUE TO A NAMING EXCEPTION!", ex);
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
        final String publicPhotoURLPath = (String) request__.getServletRequest().getAttribute("photoURL");

        new AbstractListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {

                itsNatDocument.addCodeToSend(JsCodeToSend);

                new Photo$Description(itsNatDocument__, $(Controller.Page.Main_center_main)) {

                    @Override
                    protected void init() {

                        $$(pd_photo_permalink).setAttribute("href", permaLink + "|" + location);
                        $$(pd_photo).setAttribute("src", publicPhotoURLPath);
                    }
                };

            }

            @Override
            protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
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
        return showChangeLog__ ? changeLog : toString();
    }
}

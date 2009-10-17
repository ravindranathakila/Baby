package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.entities.HumansPrivatePhoto;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.NamingException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.logic.Listeners.widgets.PhotoCRUD;
import static ai.ilikeplaces.servlets.Controller.Page.*;
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
public class ListenerHuman implements ItsNatServletRequestListener {

    final protected static String JsCodeToSend = "document.monitor = new EventMonitor(); \n" +
            "document.getItsNatDoc().addEventMonitor(document.monitor); \n";
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<HumansPublicPhoto> crudHumansPublicPhoto_ = null;
    private CrudServiceLocal<HumansPrivatePhoto> crudHumansPrivatePhoto_ = null;
    final private Logger logger = LoggerFactory.getLogger(ListenerHuman.class.getName());

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public ListenerHuman() {
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

                crudHumansPublicPhoto_ = (CrudServiceLocal<HumansPublicPhoto>) context.lookup("CrudServiceLocal");
                if (crudHumansPublicPhoto_ == null) {
                    log.append("\nVARIABLE crudHumansPublicPhoto_ IS NULL! ");
                    log.append(crudHumansPublicPhoto_);
                    break init;

                }
                crudHumansPrivatePhoto_ = (CrudServiceLocal<HumansPrivatePhoto>) context.lookup("CrudServiceLocal");
                if (crudHumansPrivatePhoto_ == null) {
                    log.append("\nVARIABLE crudHumansPrivatePhoto_ IS NULL! ");
                    log.append(crudHumansPrivatePhoto_);
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

        new AbstractListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JsCodeToSend);

                if (sessionBoundBadReferenceWrapper != null) {
                    //sessionBoundBadReferenceWrapper.boundInstance.getLoggedOnUserId()
                    HumansPublicPhoto humansPublicPhoto = crudHumansPublicPhoto_.find(HumansPublicPhoto.class, sessionBoundBadReferenceWrapper.boundInstance.getLoggedOnUserId());

                    for (final PublicPhoto publicPhoto : humansPublicPhoto.getPublicPhotos()) {
                        new PhotoCRUD(itsNatDocument__, $(Controller.Page.Main_center_main), publicPhoto) {

                            @Override
                            protected void init() {

                                $$(pc_photo_permalink).setAttribute("href", "");
                                $$(pc_photo).setAttribute("src", publicPhoto.getPublicPhotoURLPath());
                            }
                        };
                    }

                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
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
        changeLog += "20090918 crudServiceLocal was throwing a bug(exception). Should be due to garbage colection of outer class." +
                "Moved it to init. Did not pursue reason as now implementation is sane and previous was not.\n";
        changeLog += "20090924 crudServiceLocal was shifter back to the original position with validation. Not a bug. " +
                "Outer class has a reference from the inner class so never gets garbage collected.";
        return showChangeLog__ ? changeLog : toString();
    }
}

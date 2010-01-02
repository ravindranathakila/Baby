package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.FIXME;
import ai.ilikeplaces.entities.HumansPrivatePhoto;
import ai.ilikeplaces.entities.HumansPublicPhoto;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.exception.ConstructorInvokationException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.Listeners.widgets.PhotoCRUD;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractListener;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * @author Ravindranath Akila
 */
public class ListenerHuman implements ItsNatServletRequestListener {

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

                crudHumansPublicPhoto_ = (CrudServiceLocal<HumansPublicPhoto>) context.lookup("CrudServiceLocal");

                crudHumansPrivatePhoto_ = (CrudServiceLocal<HumansPrivatePhoto>) context.lookup("CrudServiceLocal");

            } catch (NamingException ex) {
                log.append("\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " SERVLET DUE TO A NAMING EXCEPTION!");
                logger.info("\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " DUE TO A NAMING EXCEPTION!", ex);
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
            throw new ConstructorInvokationException(log.toString());
        }
    }

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
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor + JSCodeToSend.FnLocationId);
                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");
                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        new SignInOn(itsNatDocument__, $(Main_login_widget)) {
                        };
                    }

                    setMainTitle:
                    {
                        try {
                            //$(mainTitle).setTextContent("Welcome to ilikeplaces!");
                        } catch (
                                @FIXME(issue = "This is very important for SEO. Contact ItsNat and find out why exception always occurs here. It says element not found.")
                                final Exception e__) {
                            logger.debug(e__.getMessage());
                        }
                    }
                    signOnDisplayLink:
                    {
                        if (getUsername() != null) {
                            final Element usersName = $(MarkupTag.P);
                            usersName.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsername());
                            $(Main_othersidebar_identity).appendChild(usersName);
                        } else {
                            final Element locationElem = $(MarkupTag.P);
                            locationElem.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0005") + location);
                            $(Main_othersidebar_identity).appendChild(locationElem);
                        }

                    }
                    setProfileLink:
                    {
                        if (getUsername() != null) {
                            $(Main_othersidebar_profile_link).setAttribute("href", RBGet.config.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0002"));
                        } else {
                            $(Main_othersidebar_profile_link).setAttribute("href", RBGet.config.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0003"));
                        }
                    }
                }
                if (getUsername() != null) {
                    //@TODO(task = "USE DBLocal")
                    //HumansPublicPhoto humansPublicPhoto = crudHumansPublicPhoto_.find(HumansPublicPhoto.class, getUsername());
                    Return<List<PublicPhoto>> r = DB.getHumanCRUDPublicPhotoLocal(true).doHumanRPublicPhoto(getUsername());

                    if (r.returnStatus() == 0) {
                        for (final PublicPhoto publicPhoto : r.returnValue()) {
                            new PhotoCRUD(itsNatDocument__, $(Controller.Page.Main_center_main), publicPhoto, getUsername()) {

                                @Override
                                protected void init() {

                                    $$(pc_photo_permalink).setAttribute(MarkupTag.A.href(), "");
                                    $$(pc_photo).setAttribute(MarkupTag.A.src(), publicPhoto.getPublicPhotoURLPath());
                                    $$(pc_photo_description).setAttribute(MarkupTag.TEXTAREA.value(), publicPhoto.getPublicPhotoDescription());
                                }
                            };
                        }
                    } else {
                        $(Main_notice).setTextContent(r.returnMsg());
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
}

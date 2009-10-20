package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.*;
import ai.ilikeplaces.exception.ExceptionConstructorInvokation;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.crud.DBLocal;
import ai.ilikeplaces.logic.crud.HumanCRUDPublicPhotoLocal;
import ai.ilikeplaces.servlets.Controller.Page;
import static ai.ilikeplaces.servlets.Controller.Page.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class PhotoCRUD extends AbstractWidgetListener {

    PublicPhoto publicPhoto;
    PrivatePhoto privatePhoto;
    final private Properties p_ = new Properties();
    private Context context = null;
    private CrudServiceLocal<PublicPhoto> crudPublicPhoto = null;
    private CrudServiceLocal<PrivatePhoto> crudPrivatePhoto = null;
    private HumanCRUDPublicPhotoLocal humanCRUDPublicPhotoLocal_ = null;
    final private Logger logger = LoggerFactory.getLogger(PhotoCRUD.class.getName());
    final String humanId;

    /**
     *
     * @param itsNatDocument__
     * @param appendToElement__
     */
    @SuppressWarnings("unchecked")
    public PhotoCRUD(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final Object t_, final String humanId) {
        super(itsNatDocument__, Page.PhotoCRUD, appendToElement__);

        this.humanId = humanId;

        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            try {

                p_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
                context = new InitialContext(p_);
                logger.info("HELLO, HASH:" + context.hashCode());

                if (t_ instanceof PublicPhoto) {
                    this.publicPhoto = (PublicPhoto) t_;
                    this.privatePhoto = null;
                    crudPublicPhoto = (CrudServiceLocal<PublicPhoto>) context.lookup("CrudServiceLocal");
                    humanCRUDPublicPhotoLocal_ = (HumanCRUDPublicPhotoLocal) context.lookup("HumanCRUDPublicPhotoLocal");
                    logger.info("HELLO, HASH:" + crudPublicPhoto.hashCode());

                } else if (t_ instanceof PrivatePhoto) {
                    this.privatePhoto = (PrivatePhoto) t_;
                    this.publicPhoto = null;
                    crudPrivatePhoto = (CrudServiceLocal<PrivatePhoto>) context.lookup("CrudServiceLocal");
                    logger.info("HELLO, HASH:" + crudPrivatePhoto.hashCode());

                } else {
                    throw new IllegalArgumentException("SORRY! THIS CLASS ONLY SUPPORTS GENERIC TYPES:" + PublicPhoto.class.getName() + " AND " + PrivatePhoto.class.getName() + ". RECIEVED TYPE:" + t_.getClass().getName());
                }


            } catch (Exception ex) {
                log.append("\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " SERVLET DUE TO AN EXCEPTION!");
                logger.info("\nSORRY! COULD NOT INITIALIZE " + getClass().getName() + " DUE TO AN EXCEPTION!", ex);
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
     */
    @Override
    protected void init() {
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        //logger.debug("DESCRIPTION:{}", $$(pc_photo_description).getTextContent());

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(pc_close), "click", new EventListener() {

            @Override
            public void handleEvent(final Event evt_) {
                toggleVisible(pc_close);
            }
        }, false);

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(pc_delete), "click", new EventListener() {

            final EventListener self = this;

            @Override
            public void handleEvent(final Event evt_) {
                DB.getHumanCRUDPublicPhotoLocal(true).doHumanDPublicPhoto(humanId, publicPhoto.getPublicPhotoId());
                remove(self, evt_.getTarget());
                //toggleVisible(pc_close);
            }
        }, false);
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(pc_photo_description), "blur", new EventListener() {

            final EventListener self = this;

            @Override
            public void handleEvent(final Event evt_) {//doHumanUPublicPhotoDescription
                logger.debug("DESCRIPTION ON EVENT:{}", ((Element) evt_.getTarget()).getTextContent());
                DB.getHumanCRUDPublicPhotoLocal(true).doHumanUPublicPhotoDescription(humanId, publicPhoto.getPublicPhotoId(), "@TODO ADD THE TEXT FROM TEXT AREA HERE!");
            }
        }, false);
    }
}

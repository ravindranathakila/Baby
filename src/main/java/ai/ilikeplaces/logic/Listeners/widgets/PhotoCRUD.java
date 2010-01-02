package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.PrivatePhoto;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.exception.ConstructorInvokationException;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

import static ai.ilikeplaces.security.xss.Trim.trimAll;
import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
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
    final private Logger logger = LoggerFactory.getLogger(PhotoCRUD.class.getName());
    final String humanId;

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    @SuppressWarnings("unchecked")
    public PhotoCRUD(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final PublicPhoto t_, final String humanId) {
        super(itsNatDocument__, Page.PhotoCRUD, appendToElement__);
        this.humanId = humanId;

        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            try {

                p_.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
                context = new InitialContext(p_);

                this.publicPhoto =  t_;


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
            throw new ConstructorInvokationException(log.toString());
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

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(pc_close), EventType.click.toString(), new EventListener() {

            @Override
            public void handleEvent(final Event evt_) {
                toggleVisible(pc_close);
            }
        }, false);

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(pc_delete), EventType.click.toString(), new EventListener() {

            final EventListener self = this;

            @Override
            public void handleEvent(final Event evt_) {
                DB.getHumanCRUDPublicPhotoLocal(true).doHumanDPublicPhoto(humanId, publicPhoto.getPublicPhotoId());
                remove(evt_.getTarget(), EventType.click, self);
                toggleVisible(pc_close);
            }
        }, false);

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(pc_photo_description), EventType.blur.toString(), new EventListener() {

            final EventListener self = this;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                DB.getHumanCRUDPublicPhotoLocal(true).doHumanUPublicPhotoDescription(humanId, publicPhoto.getPublicPhotoId(), trimAll(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value())));
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
    }
}

//package ai.ilikeplaces.logic.Listeners.widgets;
//
//import ai.ilikeplaces.doc.License;
//import ai.ilikeplaces.doc.OK;
//import ai.ilikeplaces.entities.PrivatePhoto;
//import ai.ilikeplaces.entities.PublicPhoto;
//import ai.ilikeplaces.jpa.CrudServiceLocal;
//import ai.ilikeplaces.logic.crud.DB;
//import ai.ilikeplaces.servlets.Controller.Page;
//import ai.ilikeplaces.util.AbstractWidgetListener;
//import ai.ilikeplaces.util.EventType;
//import ai.ilikeplaces.util.Loggers;
//import ai.ilikeplaces.util.MarkupTag;
//import org.itsnat.core.ItsNatDocument;
//import org.itsnat.core.ItsNatServletRequest;
//import org.itsnat.core.event.NodePropertyTransport;
//import org.itsnat.core.html.ItsNatHTMLDocument;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.w3c.dom.Element;
//import org.w3c.dom.events.Event;
//import org.w3c.dom.events.EventListener;
//import org.w3c.dom.events.EventTarget;
//import org.w3c.dom.html.HTMLDocument;
//
//import javax.naming.Context;
//import java.util.Properties;
//
//import static ai.ilikeplaces.servlets.Controller.Page.*;
//
///**
// * @author Ravindranath Akila
// */
//
//@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
//@OK
//abstract public class PhotoCRUD extends AbstractWidgetListener {
//
//    PublicPhoto publicPhoto;
//    PrivatePhoto privatePhoto;
//    final private Properties p_ = new Properties();
//    private Context context = null;
//    private CrudServiceLocal<PublicPhoto> crudPublicPhoto = null;
//    private CrudServiceLocal<PrivatePhoto> crudPrivatePhoto = null;
//    final private Logger logger = LoggerFactory.getLogger(PhotoCRUD.class.getName());
//    final String humanId;
//
//    /**
//     * @param itsNatDocument__
//     * @param appendToElement__
//     * @param publicPhoto__
//     * @param humanId__
//     */
//    public PhotoCRUD(final ItsNatServletRequest request__,  final Element appendToElement__, final PublicPhoto publicPhoto__, final String humanId__) {
//        super(request__, Page.PhotoCRUD, appendToElement__);
//        this.humanId = humanId__;
//        this.publicPhoto = publicPhoto__;
//    }
//
//    /**
//     *
//     */
//    @Override
//    protected void init(final Object... initArgs) {
//    }
//
//    @Override
//    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
//
//        itsNatHTMLDocument__.addEventListener((EventTarget) $$(pc_close), EventType.CLICK.toString(), new EventListener() {
//
//            @Override
//            public void handleEvent(final Event evt_) {
//                toggleVisible(pc_close);
//            }
//
//
//            @Override
//            public void finalize() throws Throwable {
//                Loggers.finalized(this.getClass().getName());
//                super.finalize();
//            }
//        }, false);
//
//        itsNatHTMLDocument__.addEventListener((EventTarget) $$(pc_delete), EventType.CLICK.toString(), new EventListener() {
//
//            final EventListener self = this;
//
//            @Override
//            public void handleEvent(final Event evt_) {
//                DB.getHumanCRUDPublicPhotoLocal(true).dPublicPhoto(humanId, publicPhoto.getPublicPhotoId());
//                remove(evt_.getTarget(), EventType.CLICK, self);
//                toggleVisible(pc_close);
//            }
//
//            @Override
//            public void finalize() throws Throwable {
//                Loggers.finalized(this.getClass().getName());
//                super.finalize();
//            }
//        }, false);
//
//        itsNatHTMLDocument__.addEventListener((EventTarget) $$(pc_photo_description), EventType.BLUR.toString(), new EventListener() {
//
//            final EventListener self = this;
//
//            @Override
//            public void handleEvent(final Event evt_) {
//                logger.debug("{}", ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
//                DB.getHumanCRUDPublicPhotoLocal(true).uPublicPhotoDescription(humanId, publicPhoto.getPublicPhotoId(), ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
//            }
//
//
//            @Override
//            public void finalize() throws Throwable {
//                Loggers.finalized(this.getClass().getName());
//                super.finalize();
//            }
//        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
//    }
//
//    @Override
//    public void finalize() throws Throwable {
//        Loggers.finalized(this.getClass().getName());
//        super.finalize();
//    }
//}

package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Info;
import ai.ilikeplaces.logic.validators.unit.SimpleName;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import net.sf.oval.Validator;
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
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateEventCreate extends AbstractWidgetListener {

    RefObj<String> privateEventName = null;

    RefObj<String> privateEventInfo = null;

    HumanId humanId = null;

    Long privateLocationId = null;

    final private Logger logger = LoggerFactory.getLogger(PrivateEventCreate.class.getName());

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     * @param humanId__
     */
    public PrivateEventCreate(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__, final long privateLocationId) {
        super(itsNatDocument__, Page.PrivateEventCreate, appendToElement__, humanId__, privateLocationId);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new HumanId((String) initArgs[0], true);
        this.privateLocationId = (Long) initArgs[1];

        this.privateEventName = new SimpleName();

        this.privateEventInfo = new Info();
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateEventCreateName), EventType.BLUR.toString(), new EventListener() {

            final RefObj<String> myprivateEventName = privateEventName;
            final Validator v = new Validator();
            RefObj<String> name;

            @Override
            public void handleEvent(final Event evt_) {
                name = new SimpleName(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                logger.debug("{}", name);

                if (name.validate(v) == 0) {
                    myprivateEventName.setObj(name.getObj());
                    clear($$(privateEventCreateNotice));
                } else {
                    $$(privateEventCreateNotice).setTextContent(name.getViolationAsString());
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateEventCreateInfo), EventType.BLUR.toString(), new EventListener() {

            final RefObj<String> myprivateEventInfo = privateEventInfo;
            final Validator v = new Validator();
            RefObj<String> info;

            @Override
            public void handleEvent(final Event evt_) {
                info = new Info(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                logger.debug("{}", info);

                if (info.validate(v) == 0) {
                    myprivateEventInfo.setObj(info.getObj());
                    clear($$(privateEventCreateNotice));
                } else {
                    $$(privateEventCreateNotice).setTextContent(info.getViolationAsString());
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateEventCreateSave), EventType.CLICK.toString(), new EventListener() {

            final HumanId myhumanId = humanId;
            final RefObj<String> myprivateEventName = privateEventName;
            final RefObj<String> myprivateEventInfo = privateEventInfo;
            final Long myprivateLocationId = privateLocationId;
            final Validator v = new Validator();

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "HELLO! CLICKED SAVE.");

                if (myprivateEventName.validate(v) == 0 && myprivateEventInfo.validate(v) == 0) {

                    final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).cPrivateEvent(myhumanId.getObj(), myprivateLocationId, myprivateEventName.getObjectAsValid(), myprivateEventInfo.getObjectAsValid(), "TODO DATE", "TODO DATE");
                    if (r.returnStatus() == 0) {
                        logger.debug("{}", "HELLO! SAVED.");
                        remove(evt_.getTarget(), EventType.CLICK, this);
                        logger.debug("{}", "HELLO! REMOVED CLICK.");
                        $$(privateEventCreateNotice).setTextContent(myprivateEventName.getObj() + " was created!");
                        $$(privateEventCreateSave).setAttribute(MarkupTag.A.href(),
                                new Parameter(Organize.getURL())
                                        .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                                        .append(DocOrganizeLocation, r.returnValue().getPrivateLocation().getPrivateLocationId())
                                        .append(DocOrganizeEvent, r.returnValue().getPrivateEventId())
                                        .get()
                        );
                    } else {
                        $$(privateEventCreateNotice).setTextContent(r.returnMsg());
                    }
                } else {
                    //Validation message should've already been given to the users by above individual listeners
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false);
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}
package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.RefString;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateEventDelete extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateEventDelete.class.getName());

    private RefString humanId = null;
    private Long privateEventId = null;

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public PrivateEventDelete(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__, final long privateEventId__) {
        super(itsNatDocument__, Page.PrivateEventDelete, appendToElement__, humanId__, privateEventId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new RefString((String) initArgs[0]);
        this.privateEventId = (Long) initArgs[1];

        final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).rPrivateEvent(humanId.getString(), privateEventId);
        if (r.returnStatus() == 0) {
            $$(privateEventDeleteName).setTextContent(r.returnValue().getPrivateEventName());
            $$(privateEventDeleteInfo).setTextContent(r.returnValue().getPrivateEventInfo());
        } else {
            $$(privateEventDeleteNotice).setTextContent(r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateEventDelete), EventType.click.toString(), new EventListener() {

            final RefString myhumanId = humanId;
            final Long myprivateEventId = privateEventId;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "HELLO! CLICKED DELETE.");

                final Return<Boolean> r = DB.getHumanCrudPrivateEventLocal(true).dPrivateEvent(myhumanId.getString(), myprivateEventId);
                if (r.returnStatus() == 0) {
                    logger.debug("{}", "HELLO! DELETED. DB REPLY:" + r.returnValue());
                    remove(evt_.getTarget(), EventType.click, this);
                    logger.debug("{}", "HELLO! REMOVED CLICK.");
                    clear($$(privateEventDeleteNotice));
                } else {
                    $$(privateEventDelete).setTextContent(r.returnMsg());
                }


            }
        }, false);

    }
}
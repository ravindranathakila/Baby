package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateLocation;
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
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

// @License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateLocationDelete extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateLocationDelete.class.getName());

    private RefString humanId = null;
    private Long privateLocationId = null;

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public PrivateLocationDelete(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__, final long privateLocationId__) {
        super(itsNatDocument__, Page.PrivateLocationDelete, appendToElement__, humanId__, privateLocationId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new RefString((String) initArgs[0]);
        this.privateLocationId = (Long) initArgs[1];

        final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).rPrivateLocation(humanId.getString(), privateLocationId);
        if (r.returnStatus() == 0) {
            $$(privateLocationDeleteName).setTextContent(r.returnValue().getPrivateLocationName());
            $$(privateLocationDeleteInfo).setTextContent(r.returnValue().getPrivateLocationInfo());
        } else {
            $$(privateLocationDeleteNotice).setTextContent(r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationDelete), EventType.click.toString(), new EventListener() {

            final RefString myhumanId = humanId;
            final Long myprivateLocationId = privateLocationId;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "HELLO! CLICKED DELETE.");

                final Return<Boolean> r = DB.getHumanCrudPrivateLocationLocal(true).dPrivateLocation(myhumanId.getString(), myprivateLocationId);
                if (r.returnStatus() == 0) {
                    logger.debug("{}", "HELLO! DELETED. DB REPLY:" + r.returnValue());
                    remove(evt_.getTarget(), EventType.click, this);
                    logger.debug("{}", "HELLO! REMOVED CLICK.");
                    clear($$(privateLocationDeleteNotice));
                } else {
                    $$(privateLocationDelete).setTextContent(r.returnMsg());
                }


            }
        }, false);

    }
}
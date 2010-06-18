package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.widgets.Button;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateEventView extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateEventView.class.getName());

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     * @param humanId__
     * @param privateEventId__
     */
    public PrivateEventView(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__, final long privateEventId__) {
        super(itsNatDocument__, Page.PrivateEventView, appendToElement__, humanId__, privateEventId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        final String humanId = (String) initArgs[0];
        final long privateEventId = (Long) initArgs[1];

        final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEvent(humanId, privateEventId);


        LoggerFactory.getLogger(PrivateEventView.class.getName()).debug(r.toString());

        if (r.returnStatus() == 0) {
            LoggerFactory.getLogger(PrivateEventView.class.getName()).debug("Setting values");
            $$(privateEventViewName).setTextContent(r.returnValue().getPrivateEventName());
            $$(privateEventViewInfo).setTextContent(r.returnValue().getPrivateEventInfo());
            new Button(itsNatDocument_, $$(privateEventViewLink), "Link to " + r.returnValue().getPrivateEventName(), false, r.returnValue()) {
                PrivateEvent privateEvent = null;

                @Override
                protected void init(final Object... initArgs) {
                    privateEvent = (PrivateEvent) (((Object[]) initArgs[2])[0]);
                    SetLocationLink:
                    {
                        setLink:
                        {
                            $$(GenericButtonLink).setAttribute(MarkupTag.A.href(),
                                    new Parameter(Organize.getURL())
                                            .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                                            .append(DocOrganizeLocation, r.returnValue().getPrivateLocation().getPrivateLocationId())
                                            .append(DocOrganizeEvent, privateEvent.getPrivateEventId())
                                            .get()
                            );
                        }
                        setImage:
                        {
                            $$(GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.config.getString(RBGet.url_CDN_STATIC) + "arrow-right.gif");
                        }
                    }
                }
            };

        } else {
            $$(privateEventViewNotice).setTextContent(r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        //No events as this is just a view widget/ Edit button???
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}
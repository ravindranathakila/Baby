package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.faces.ValidatorFace;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
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

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateLocationCreate extends AbstractWidgetListener {

    RefStringFace privateLocationName = null;

    RefStringFace privateLocationInfo = null;

    RefString humanId = null;

    final private Logger logger = LoggerFactory.getLogger(PrivateLocationCreate.class.getName());

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public PrivateLocationCreate(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final String humanId__) {
        super(itsNatDocument__, Page.PrivateLocationCreate, appendToElement__, humanId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new RefString((String) initArgs[0]);
        this.privateLocationName = new RefString(null);
        this.privateLocationInfo = new RefString(null);
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateName), EventType.blur.toString(), new EventListener() {

            final RefStringFace myprivateLocationName = privateLocationName;

            @Override
            public void handleEvent(final Event evt_) {
                final String name = ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value());
                logger.debug("{}", name);

                ReturnParamsFace returnParams = ((Factory<ValidatorFace>) ValidatorFace.impl.getFactory()).getInstance().isPrivateLocationName(name).returnValue();

                if ((Boolean) returnParams.get(0)) {
                    myprivateLocationName.setString((String) returnParams.get(1));
                    clear($$(privateLocationCreateNotice));
                } else {
                    $$(privateLocationCreateNotice).setTextContent((String) returnParams.get(1));
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateInfo), EventType.blur.toString(), new EventListener() {

            final RefStringFace myprivateLocationInfo = privateLocationInfo;

            @Override
            public void handleEvent(final Event evt_) {
                final String info = ((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value());
                logger.debug("{}", info);

                ReturnParamsFace returnParams = ((Factory<ValidatorFace>) ValidatorFace.impl.getFactory()).getInstance().isPrivateLocationName(info).returnValue();

                if ((Boolean) returnParams.get(0)) {
                    myprivateLocationInfo.setString((String) returnParams.get(1));
                    clear($$(privateLocationCreateNotice));
                } else {
                    $$(privateLocationCreateNotice).setTextContent((String) returnParams.get(1));
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateSave), EventType.click.toString(), new EventListener() {

            final RefString myhumanId = humanId;
            final RefStringFace myprivateLocationName = privateLocationName;
            final RefStringFace myprivateLocationInfo = privateLocationInfo;

            @Override
            public void handleEvent(final Event evt_) {
                logger.debug("{}", "HELLO! CLICKED SAVE.");

                if (myprivateLocationName.getString() != null && myprivateLocationInfo.getString() != null &&
                        !myprivateLocationName.getString().equals("") && !myprivateLocationInfo.getString().equals("")) {
                    final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).cPrivateLocation(myhumanId.getString(), myprivateLocationName.getString(), myprivateLocationInfo.getString());
                    if (r.returnStatus() == 0) {
                        logger.debug("{}", "HELLO! SAVED.");
                        remove(evt_.getTarget(), EventType.click, this);
                        logger.debug("{}", "HELLO! REMOVED CLICK.");
                        clear($$(privateLocationCreateNotice));
                    } else {
                        $$(privateLocationCreateNotice).setTextContent(r.returnMsg());
                    }
                } else {
                    $$(privateLocationCreateNotice).setTextContent("Please specify BOTH name AND information.");
                }


            }
        }, false);
    }
}
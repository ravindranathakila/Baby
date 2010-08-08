package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.Info;
import ai.ilikeplaces.logic.validators.unit.SimpleName;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import net.sf.oval.Validator;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
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

    RefObj<String> privateLocationName = null;

    RefObj<String> privateLocationInfo = null;

    HumanId humanId = null;

    final private Logger logger = LoggerFactory.getLogger(PrivateLocationCreate.class.getName());

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId__
     */
    public PrivateLocationCreate(final ItsNatServletRequest request__,  final Element appendToElement__, final String humanId__) {
        super(request__, Page.PrivateLocationCreate, appendToElement__, humanId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new HumanId();
        humanId.setObjAsValid((String) initArgs[0]);

        this.privateLocationName = new SimpleName();

        this.privateLocationInfo = new Info();
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateName), EventType.BLUR.toString(), new EventListener() {

            final RefObj<String> myprivateLocationName = privateLocationName;
            final Validator v = new Validator();
            RefObj<String> name;

            @Override
            public void handleEvent(final Event evt_) {
                name = new SimpleName(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                logger.debug("{}", name);

                if (name.validate(v) == 0) {
                    myprivateLocationName.setObj(name.getObj());
                    clear($$(PrivateLocationCreateCNotice));
                } else {
                    $$(PrivateLocationCreateCNotice).setTextContent(name.getViolationAsString());
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateInfo), EventType.BLUR.toString(), new EventListener() {

            final RefObj<String> myprivateLocationInfo = privateLocationInfo;
            final Validator v = new Validator();
            RefObj<String> info;

            @Override
            public void handleEvent(final Event evt_) {
                info = new Info(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                logger.debug("{}", info);

                if (info.validate(v) == 0) {
                    myprivateLocationInfo.setObj(info.getObj());
                    clear($$(PrivateLocationCreateCNotice));
                } else {
                    $$(PrivateLocationCreateCNotice).setTextContent(info.getViolationAsString());
                }
            }

            @Override
            public void finalize() throws Throwable {
                Loggers.finalized(this.getClass().getName());
                super.finalize();
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationCreateSave), EventType.CLICK.toString(), new EventListener() {

            final HumanId myhumanId = humanId;
            final RefObj<String> myprivateLocationName = privateLocationName;
            final RefObj<String> myprivateLocationInfo = privateLocationInfo;
            final Validator v = new Validator();

            @Override
            public void handleEvent(final Event evt_) {
                final SmartLogger sl;


                if (myprivateLocationName.validate(v) == 0 && myprivateLocationInfo.validate(v) == 0) {
                    sl = SmartLogger.start(Loggers.LEVEL.DEBUG, "Saving private location", 10000, null, true);
                    final Return<PrivateLocation> r = DB.getHumanCrudPrivateLocationLocal(true).cPrivateLocation(myhumanId, myprivateLocationName.getObj(), myprivateLocationInfo.getObj());
                    if (r.returnStatus() == 0) {
                        remove(evt_.getTarget(), EventType.CLICK, this);
                        $$(PrivateLocationCreateCNotice).setTextContent(myprivateLocationName.getObj() + " was created!");
                        $$(privateLocationCreateSave).setAttribute(MarkupTag.A.href(), Controller.Page.Organize.getURL() + "?"
                                + Controller.Page.DocOrganizeCategory + "=" + 2 + "&"
                                + Controller.Page.DocOrganizeLocation + "=" + r.returnValue().getPrivateLocationId());
                        sl.complete(Loggers.DONE + r.returnMsg());
                    } else {
                        $$(PrivateLocationCreateCNotice).setTextContent(r.returnMsg());
                        sl.complete(r.returnMsg());
                    }
                } else {
                    if (myprivateLocationName.validate(v) != 0) {
                        $$(PrivateLocationCreateCNotice).setTextContent(myprivateLocationName.getViolationAsString());
                    } else if (myprivateLocationInfo.validate(v) != 0) {
                        $$(PrivateLocationCreateCNotice).setTextContent(myprivateLocationInfo.getViolationAsString());
                    }
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
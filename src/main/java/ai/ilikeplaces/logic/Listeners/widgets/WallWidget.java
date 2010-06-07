package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.WallEntry;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class WallWidget extends AbstractWidgetListener {

    HumanId humanId;

    final WallEntry wallAppend = new WallEntry("");
    Long privateEventId = null;


    public WallWidget(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId, final long privateEventId__) {
        super(itsNatDocument__, Page.WallHandler, appendToElement__, humanId, privateEventId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = (HumanId) ((HumanId) initArgs[0]).getSelfAsValid();
        this.privateEventId = (Long) initArgs[1];
        for (final String textFragment : DB.getHumanCrudPrivateEventLocal(true).
                rPrivateEventReadWall(humanId, privateEventId).returnValueBadly().getWallContent().split("\n")) {
            $$(Page.wallContent).appendChild(ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(textFragment).get());
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        UCProcessWallText:
        {

            itsNatHTMLDocument__.addEventListener((EventTarget) $$(Page.wallAppend), EventType.BLUR.toString(), new EventListener() {

                private HumanId myhumanId = humanId;

                @Override
                public void handleEvent(final Event evt_) {
                    wallAppend.setObj(((Element) evt_.getCurrentTarget()).getAttribute(MarkupTag.TEXTAREA.value()));
                    if (wallAppend.validate() == 0) {
                        wallAppend.setObj(wallAppend.getObj());
                        clear($$(Page.wallNotice));
                    } else {
                        $$(Page.wallNotice).setTextContent(wallAppend.getViolationAsString());
                        wallAppend.setObj("");
                    }
                }

                @Override
                public void finalize() throws Throwable {
                    Loggers.finalized(this.getClass().getName());
                    super.finalize();
                }
            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
        }

        UCWallSubmit:
        {

            itsNatHTMLDocument__.addEventListener((EventTarget) $$(Page.wallSubmit), EventType.CLICK.toString(), new EventListener() {

                private HumanId myhumanId = humanId;
                private Long myprivateEventId = privateEventId;
                private boolean autoupdating = false;

                @Override
                public void handleEvent(final Event evt_) {
/*                    UCStartAutoUpdateThread:
                    {
                        if(!autoupdating){
                            new Thread();
                            autoupdating = true;
                        }
                    }*/

                    Loggers.USER.info(myhumanId.getObj() + " enters text:" + wallAppend.getObj());
                    if (wallAppend.validate() == 0) {
                        if (!wallAppend.getObj().equals("")) {

                            Loggers.USER.info(myhumanId.getObj() + " enters text:" + wallAppend.getObj());

                            final Return<Wall> r = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddToWall(myhumanId, myprivateEventId,
                                    DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(myhumanId.getObj()).getDisplayName() + ">"
                                            + wallAppend.getObj()
                                            + "\n\n");

                            if (r.returnStatus() == 0) {
                                $$(Page.wallAppend).setAttribute(MarkupTag.TEXTAREA.value(), "");
                                clear($$(Page.wallContent));
                                final Wall wall = (DB.getHumanCrudPrivateEventLocal(true).rPrivateEventReadWall(myhumanId, myprivateEventId).returnValueBadly());
                                for (final String textFragment : wall.getWallContent().split("\n")) {
                                    $$(Page.wallContent).appendChild(ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(textFragment).get());
                                }
                            } else {
                                $$(Page.wallNotice).setTextContent(r.returnMsg());
                            }
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
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}
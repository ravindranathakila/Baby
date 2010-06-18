package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.EventType;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import ai.ilikeplaces.util.Return;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: Jun 17, 2010
 * Time: 12:17:36 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class WallWidgetPrivateEvent extends WallWidget {

    HumanId humanId;
    Long privateEventId = null;

    public WallWidgetPrivateEvent(final ItsNatDocument itsNatDocument__, final Element appendToElement__, final HumanId humanId, final long privateEventId__) {
        super(itsNatDocument__, appendToElement__, humanId, privateEventId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = (HumanId) ((HumanId) initArgs[0]).getSelfAsValid();
        this.privateEventId = (Long) initArgs[1];
        for (final Msg msg : DB.getHumanCrudPrivateEventLocal(true).
                rPrivateEventReadWall(humanId, privateEventId).returnValueBadly().getWallMsgs()) {
            new UserProperty(itsNatDocument_, $$(Controller.Page.wallContent), new HumanId(msg.getMsgMetadata())) {
                protected void init(final Object... initArgs) {
                    $$(Controller.Page.user_property_content).setTextContent(msg.getMsgContent());
                }
            };
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.wallSubmit), EventType.CLICK.toString(), new EventListener() {

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


                        final Return<Wall> r = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddEntryToWall(myhumanId,
                                myhumanId,
                                myprivateEventId,
                                wallAppend.getObj());


                        if (r.returnStatus() == 0) {
                            $$(Controller.Page.wallAppend).setAttribute(MarkupTag.TEXTAREA.value(), "");
                            clear($$(Controller.Page.wallContent));
                            final Wall wall = (DB.getHumanCrudPrivateEventLocal(true).rPrivateEventReadWall(myhumanId, myprivateEventId).returnValueBadly());
                            for (final Msg msg : wall.getWallMsgs()) {

                                new UserProperty(itsNatDocument_, $$(Controller.Page.wallContent), new HumanId(msg.getMsgMetadata())) {
                                    protected void init(final Object... initArgs) {
                                        $$(Controller.Page.user_property_content).setTextContent(msg.getMsgContent());
                                    }
                                };
                            }
                        } else {
                            $$(Controller.Page.wallNotice).setTextContent(r.returnMsg());
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

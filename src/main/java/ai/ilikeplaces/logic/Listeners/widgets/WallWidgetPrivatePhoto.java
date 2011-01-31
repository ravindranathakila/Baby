package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.DOCUMENTATION;
import ai.ilikeplaces.doc.LOGIC;
import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 17, 2010
 * Time: 12:17:36 AM
 */


@DOCUMENTATION(
        LOGIC = @LOGIC(@NOTE(
                {
                        "FetchToMail Missing - Because photos are discussed AFTER the event. Hence no need interaction for event except during organizing. Just email notification is sufficient.",
                        "Load - Should load upon scroll or click, since loading all walls on album photos is too much load on server, and also diverts user attention.",
                        "Scroll Load With Image Click - Because a user might do a sudden scroll down to a relevant image. If scroll, this would mean chaos, both on browser and server load."
                }))
)
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class WallWidgetPrivatePhoto extends WallWidget {

    private static final String WALL_SUBIT_FROM_EMAIL = "ai/ilikeplaces/widgets/WallSubmitFromEmail.xhtml";

    HumanId humanId;
    Long privatePhotoId = null;
    private static final String NULL = "null";
    private static final String WALL_ENTRY = "wall_entry=";
    private static final String ENTERS_TEXT = " enters text:";

    public WallWidgetPrivatePhoto(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId, final long privatePhotoId__) {
        super(request__, appendToElement__, humanId, privatePhotoId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = ((HumanId) initArgs[0]).getSelfAsValid();
        this.privatePhotoId = (Long) initArgs[1];

        final String wall_entry = request.getServletRequest().getParameter(WallWidget.PARAM_WALL_ENTRY);
        Loggers.DEBUG.debug(WALL_ENTRY + (wall_entry != null ? wall_entry : NULL));


        final Return<Wall> aReturn = DB.getHumanCRUDPrivatePhotoLocal(true).
                readWall(humanId, (Obj) new Obj<Long>(privatePhotoId).getSelfAsValid());

        final Wall wall = aReturn.returnValueBadly();

        final StringBuilder b = new StringBuilder("");
        for (final Msg msg : wall.getWallMsgs()) {
            b.append(
                    new UserProperty(
                            request,
                            $$(Controller.Page.wallContent),
                            ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(msg.getMsgContent()).get(),
                            new HumanId(msg.getMsgMetadata())) {
                    }.fetchToEmail
            );
        }

        if (wall.getWallMutes().contains(humanId)) {
            $$(Controller.Page.wallMute).setTextContent(LISTEN);
        } else {
            $$(Controller.Page.wallMute).setTextContent(MUTE);
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.wallSubmit), EventType.CLICK.toString(), new EventListener() {

            private HumanId myhumanId = humanId;
            private Long myprivatePhotoId = privatePhotoId;

            @Override
            public void handleEvent(final Event evt_) {

                Loggers.USER.info(myhumanId.getObj() + ENTERS_TEXT + wallAppend.getObj());
                if (wallAppend.validate() == 0) {
                    if (!wallAppend.getObj().equals("")) {

                        Loggers.USER.info(myhumanId.getObj() + ENTERS_TEXT + wallAppend.getObj());


                        final Return<Wall> r = DB.getHumanCRUDPrivatePhotoLocal(true).addEntryToWall(myhumanId,
                                myhumanId,
                                (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid(),
                                wallAppend.getObj());


                        if (r.returnStatus() == 0) {
                            $$(Controller.Page.wallAppend).setAttribute(MarkupTag.TEXTAREA.value(), "");
                            wallAppend.setObj("");

                            clear($$(Controller.Page.wallContent));
                            final Wall wall = (DB.getHumanCRUDPrivatePhotoLocal(true).readWall(myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid()).returnValueBadly());
                            final StringBuilder b = new StringBuilder("");
                            for (final Msg msg : wall.getWallMsgs()) {
                                b.append(
                                        new UserProperty(
                                                request,
                                                $$(Controller.Page.wallContent),
                                                ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(msg.getMsgContent()).get(),
                                                new HumanId(msg.getMsgMetadata())) {
                                        }.fetchToEmail
                                );
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


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(Controller.Page.wallMute), EventType.CLICK.toString(), new EventListener() {

            private HumanId myhumanId = humanId;
            private Long myprivatePhotoId = privatePhotoId;

            @Override
            public void handleEvent(final Event evt_) {

                if (DB.getHumanCRUDPrivatePhotoLocal(true).readWall(myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid()).returnValueBadly().getWallMutes().contains(myhumanId)) {
                    DB.getHumanCRUDPrivatePhotoLocal(true).unmuteWall(myhumanId, myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid());
                    $$(evt_).setTextContent(MUTE);

                } else {
                    DB.getHumanCRUDPrivatePhotoLocal(true).muteWall(myhumanId, myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid());
                    $$(evt_).setTextContent(LISTEN);
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

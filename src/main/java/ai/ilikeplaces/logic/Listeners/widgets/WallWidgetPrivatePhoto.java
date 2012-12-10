package ai.ilikeplaces.logic.Listeners.widgets;

import ai.doc.License;
import ai.doc._doc;
import ai.doc._logic;
import ai.doc._note;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.Wall;
import ai.ilikeplaces.entities.etc.RefreshSpec;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.util.*;
import ai.util.HumanId;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jun 17, 2010
 * Time: 12:17:36 AM
 */


@_doc(
        LOGIC = @_logic(@_note(
                {
                        "FetchToMail Missing - Because photos are discussed AFTER the event. Hence no need interaction for event except during organizing. Just email notification is sufficient.",
                        "Load - Should load upon scroll or click, since loading all walls on album photos is too much load on server, and also diverts user attention.",
                        "Scroll Load With Image Click - Because a user might do a sudden scroll down to a relevant image. If scroll, this would mean chaos, both on browser and server load."
                }))
)
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class WallWidgetPrivatePhoto extends WallWidget {

    private static final String WALL_SUBIT_FROM_EMAIL = "ai/ilikeplaces/widgets/WallSubmitFromEmail.xhtml";
    private static final RefreshSpec REFRESH_SPEC = new RefreshSpec("wallMsgs", "wallMutes");
    private static final String TALK_ON_THIS_PHOTO = "talk.on.this.photo";

    HumanId humanId;

    Long privatePhotoId = null;

    Map<String, HumansIdentity> wallProspects;


    private static final String NULL = "null";
    private static final String WALL_ENTRY = "wall_entry=";
    private static final String ENTERS_TEXT = " enters text:";


    public WallWidgetPrivatePhoto(final ItsNatServletRequest request__, final Element appendToElement__, final HumanId humanId, final long privatePhotoId__, final List<HumansIdentity> wallProspects) {
        super(request__, appendToElement__, humanId, privatePhotoId__, wallProspects);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = ((HumanId) initArgs[0]).getSelfAsValid();
        this.privatePhotoId = (Long) initArgs[1];
        List<HumansIdentity> listOfProspects = (List<HumansIdentity>) initArgs[2];
        wallProspects = new HashMap<String, HumansIdentity>(listOfProspects.size());
        for (final HumansIdentity humansIdentity : listOfProspects) {
            wallProspects.put(humansIdentity.getHumanId(), humansIdentity);
        }

        final String wall_entry = request.getServletRequest().getParameter(WallWidget.PARAM_WALL_ENTRY);
        Loggers.DEBUG.debug(WALL_ENTRY + (wall_entry != null ? wall_entry : NULL));


        final Return<Wall> aReturn = DB.getHumanCRUDPrivatePhotoLocal(true).
                readWall(humanId, (Obj) new Obj<Long>(privatePhotoId).getSelfAsValid(), REFRESH_SPEC);

        final Wall wall = aReturn.returnValueBadly();

        final StringBuilder b = new StringBuilder("");
        for (final Msg msg : wall.getWallMsgs()) {
            b.append(
                    new UserProperty(
                            request,
                            $$(WallWidgetIds.wallContent),
                            ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(msg.getMsgContent()).get(),
                            wallProspects.get(msg.getMsgMetadata())) {
                    }.fetchToEmail
            );
        }

        final HumansIdentity currUserAsVisitorHI = UserProperty.HUMANS_IDENTITY_CACHE.get((humanId.getHumanId()), "");

        super.setWallProfileName(currUserAsVisitorHI.getHuman().getDisplayName());
        super.setWallProfilePhoto(UserProperty.formatProfilePhotoUrl(currUserAsVisitorHI.getHumansIdentityProfilePhoto()));
        //Change property key please super.setWallTitle(RBGet.gui().getString(TALK_ON_THIS_PHOTO));

        $$displayWallAsMuted($$(WallWidgetIds.wallMute), wall.getWallMutes().contains(humanId));
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(WallWidgetIds.wallSubmit), EventType.CLICK.toString(), new EventListener() {

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
                            $$(WallWidgetIds.wallAppend).setAttribute(MarkupTag.TEXTAREA.value(), "");
                            wallAppend.setObj("");

                            clear($$(WallWidgetIds.wallContent));
                            final Wall wall = (DB.getHumanCRUDPrivatePhotoLocal(true).readWall(myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid(), REFRESH_SPEC).returnValueBadly());
                            final StringBuilder b = new StringBuilder("");
                            for (final Msg msg : wall.getWallMsgs()) {
                                b.append(
                                        new UserProperty(
                                                request,
                                                $$(WallWidgetIds.wallContent),
                                                ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(msg.getMsgContent()).get(),
                                                new HumanId(msg.getMsgMetadata())) {
                                        }.fetchToEmail
                                );
                            }

                            //final PrivateEvent pe = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventAsAny(myhumanId.getObj(), myprivateEventId).returnValue();
                            for (final HumansIdentity hpe : wallProspects.values()) {
                                if (!wall.getWallMutes().contains(hpe) && !hpe.getHumanId().equals(myhumanId.getObj())) {
                                    //SendMail.getSendMailLocal().sendAsHTMLAsynchronously(hpe.getHumanId(), pe.getPrivateEventName(), fetchToEmail + b.toString());
                                    DB.getHumanCRUDHumansUnseenLocal(false).addEntry(hpe.getHumanId(), wall.getWallId());
                                }
                            }

                        } else {
                            $$(WallWidgetIds.wallNotice).setTextContent(r.returnMsg());
                        }
                        $$sendJS(JSCodeToSend.UpdateDocument);
                    }
                }
            }


        }, false);

        UCProcessWallText:
        {

            itsNatDocument_.addEventListener((EventTarget) $$(WallWidgetIds.wallAppend), EventType.BLUR.toString(), new EventListener() {

                private HumanId myhumanId = humanId;
                private Long myprivatePhotoId = privatePhotoId;

                @Override
                public void handleEvent(final Event evt_) {
                    UCRefreshingWall:
                    {
                        $$(WallWidgetIds.wallAppend).setAttribute(MarkupTag.TEXTAREA.value(), "");
                        wallAppend.setObj("");

                        clear($$(WallWidgetIds.wallContent));
                        final Wall wall = (DB.getHumanCRUDPrivatePhotoLocal(true).readWall(myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid(), REFRESH_SPEC).returnValueBadly());
                        final StringBuilder b = new StringBuilder("");
                        for (final Msg msg : wall.getWallMsgs()) {
                            b.append(
                                    new UserProperty(
                                            request,
                                            $$(WallWidgetIds.wallContent),
                                            ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(msg.getMsgContent()).get(),
                                            new HumanId(msg.getMsgMetadata())) {
                                    }.fetchToEmail
                            );
                        }
                    }
                }

            }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));
        }


        itsNatHTMLDocument__.addEventListener((EventTarget) $$(WallWidgetIds.wallMute), EventType.CLICK.toString(), new EventListener() {

            private HumanId myhumanId = humanId;
            private Long myprivatePhotoId = privatePhotoId;

            @Override
            public void handleEvent(final Event evt_) {

                if (DB.getHumanCRUDPrivatePhotoLocal(true).readWall(myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid(), REFRESH_SPEC).returnValueBadly().getWallMutes().contains(myhumanId)) {
                    DB.getHumanCRUDPrivatePhotoLocal(true).unmuteWall(myhumanId, myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid());
                    $$displayWallAsMuted(evt_, false);

                } else {
                    DB.getHumanCRUDPrivatePhotoLocal(true).muteWall(myhumanId, myhumanId, (Obj) new Obj<Long>(myprivatePhotoId).getSelfAsValid());
                    $$displayWallAsMuted(evt_, true);
                }
            }


        }, false);

    }
}

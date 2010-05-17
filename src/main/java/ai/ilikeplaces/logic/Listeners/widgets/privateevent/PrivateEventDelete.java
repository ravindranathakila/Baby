package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.Button;
import ai.ilikeplaces.logic.Listeners.widgets.MemberHandler;
import ai.ilikeplaces.logic.Listeners.widgets.WallWidget;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateEventDelete extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateEventDelete.class.getName());

    private HumanId humanId = null;
    private Long privateEventId = null;
    Return<PrivateEvent> r;
    List<HumansNetPeople> possibilities;

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
        this.humanId = new HumanId((String) initArgs[0]);
        this.privateEventId = (Long) initArgs[1];

        r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEvent(humanId.getObj(), privateEventId);
        if (r.returnStatus() == 0) {
            $$(privateEventDeleteName).setTextContent(r.returnValue().getPrivateEventName());
            $$(privateEventDeleteInfo).setTextContent(r.returnValue().getPrivateEventInfo());
            new Button(itsNatDocument_, $$(privateEventDeleteLink), "Link to " + r.returnValue().getPrivateEventName(), false, r.returnValue()) {
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
            new WallWidget(itsNatDocument_,$$(Page.privateEventWall),humanId,r.returnValue().getPrivateEventId()){
            };
        } else {
            $$(privateEventDeleteNotice).setTextContent(r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateEventDelete), EventType.CLICK.toString(), new EventListener() {

            final HumanId myhumanId = humanId;
            final Long myprivateEventId = privateEventId;

            @Override
            public void handleEvent(final Event evt_) {
                Loggers.USER.info(humanId.getObj()+" clicked delete for private event "+myprivateEventId);

                final Return<Boolean> r = DB.getHumanCrudPrivateEventLocal(true).dPrivateEvent(myhumanId.getObj(), myprivateEventId);
                if (r.returnStatus() == 0) {
                    Loggers.USER.info(humanId.getObj()+" clicked deleted private event "+r.returnValue());
                    remove(evt_.getTarget(), EventType.CLICK, this);
                    clear($$(privateEventDeleteNotice));
                } else {
                    $$(privateEventDelete).setTextContent(r.returnMsg());
                }


            }
        }, false, JSCodeToSend.RefreshPage);

        final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId);
        this.possibilities = user.getHumansNetPeoples();

        AddRemoveOwners:
        {
            new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateEvent>>(
                    itsNatDocument_,
                    $$(privateEventDeleteOwners),
                    user.getHumansNet(),
                    possibilities,
                    r.returnValue().getPrivateEventOwners(),
                    new Save<Return<PrivateEvent>>() {

                        final long myprivateEventId = r.returnValue().getPrivateEventId();

                        @Override
                        public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddOwner(humanId, myprivateEventId, humansFriend);
                        }
                    },
                    new Save<Return<PrivateEvent>>() {

                        final long myprivateEventId = r.returnValue().getPrivateEventId();

                        @Override
                        public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateEventLocal(true).uPrivateEventRemoveOwner(humanId, myprivateEventId, humansFriend);
                        }
                    }) {
            };
        }
        AddRemoveVisitors:
        {
            new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateEvent>>(
                    itsNatDocument_,
                    $$(privateEventDeleteVisitors),
                    user.getHumansNet(),
                    possibilities,
                    r.returnValue().getPrivateEventViewers(),
                    new Save<Return<PrivateEvent>>() {

                        final long myprivateEventId = r.returnValue().getPrivateEventId();

                        @Override
                        public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddVisitor(humanId, myprivateEventId, humansFriend);
                        }
                    },
                    new Save<Return<PrivateEvent>>() {

                        final long myprivateEventId = r.returnValue().getPrivateEventId();

                        @Override
                        public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                            return DB.getHumanCrudPrivateEventLocal(true).uPrivateEventRemoveVisitor(humanId, myprivateEventId, humansFriend);
                        }
                    }) {
            };
            AddRemoveInvitee:
            {
                new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateEvent>>(
                        itsNatDocument_,
                        $$(privateEventDeleteInvitees),
                        user.getHumansNet(),
                        possibilities,
                        r.returnValue().getPrivateEventInvites(),
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = r.returnValue().getPrivateEventId();

                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                                return DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddInvite(humanId, myprivateEventId, humansFriend);
                            }
                        },
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = r.returnValue().getPrivateEventId();

                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                                return DB.getHumanCrudPrivateEventLocal(true).uPrivateEventRemoveInvite(humanId, myprivateEventId, humansFriend);
                            }
                        });
            }
        }
    }
}
package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.doc.License;
import ai.doc.WARNING;
import ai.doc._ok;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.etc.HumansFriend;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import ai.reaver.HumanId;
import ai.reaver.Return;
import org.itsnat.core.ItsNatServletRequest;
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
@_ok
abstract public class PrivateEventDelete extends AbstractWidgetListener {


    private static final String EVENT_NAME = "_eventName_";
    final private Logger logger = LoggerFactory.getLogger(PrivateEventDelete.class.getName());

    private HumanId humanId = null;

    private Long privateEventId = null;

    @WARNING("Underlying widgets expect a fully refreshed PrivateEvent with respect to owners, visitors and viewers")
    Return<PrivateEvent> privateEventReturn;

    List<HumansNetPeople> possibilities;

    private static final String ARROW_RIGHT_GIF = "arrow-right.gif";

    private String eventLink = "";

    public static enum PrivateEventDeleteIds implements WidgetIds {
        privateEventDeleteName,
        privateEventDeleteInfo,
        privateEventDeleteNotice,
        privateEventDelete,
        privateEventDeleteLink,
        privateEventDeleteOwners,
        privateEventDeleteVisitors,
        privateEventDeleteInvitees,
        privateEventDeleteWall,
        privateEventDeleteAlbum,
        privateEventDeleteLocationMap,
    }

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId__
     * @param privateEventId__
     * @param detailedMode__
     */
    public PrivateEventDelete(final ItsNatServletRequest request__, final Element appendToElement__, final String humanId__, final long privateEventId__, final boolean detailedMode__) {
        super(request__, Page.PrivateEventDelete, appendToElement__, humanId__, privateEventId__, detailedMode__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new HumanId((String) initArgs[0]);
        this.privateEventId = (Long) initArgs[1];

        privateEventReturn = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventAsAny(humanId.getObj(), privateEventId);
        if (privateEventReturn.returnStatus() == 0) {
            $$(PrivateEventDeleteIds.privateEventDeleteName).setTextContent(privateEventReturn.returnValue().getPrivateEventName());
            $$(PrivateEventDeleteIds.privateEventDeleteInfo).setTextContent(privateEventReturn.returnValue().getPrivateEventInfo());

            eventLink = new Parameter("http://www.ilikeplaces.com" + Organize.getURL())
                    .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                    .append(DocOrganizeLocation, privateEventReturn.returnValue().getPrivateLocation().getPrivateLocationId())
                    .append(DocOrganizeEvent, privateEventReturn.returnValue().getPrivateEventId())
                    .get();

            if ((Boolean) initArgs[2]) {
                new WallWidgetPrivateEvent(request, $$(PrivateEventDeleteIds.privateEventDeleteWall), humanId, privateEventReturn.returnValue().getPrivateEventId());
                new AlbumManager(request, $$(PrivateEventDeleteIds.privateEventDeleteAlbum), humanId, privateEventReturn.returnValue());

                final GeoCoord gc = new GeoCoord();
                gc.setObj(privateEventReturn.returnValue().getPrivateLocation().getPrivateLocationLatitude(), privateEventReturn.returnValue().getPrivateLocation().getPrivateLocationLongitude());
                gc.validateThrow();

                $$(PrivateEventDeleteIds.privateEventDeleteLocationMap).setAttribute(MarkupTag.IMG.src(),
                        new Parameter("http://maps.google.com/maps/api/staticmap")
                                .append("sensor", "false", true)
                                .append("center", gc.toString())
                                .append("zoom", "14")
                                .append("size", "150x150")
                                .append("format", "jpg")
                                .append("markers", "color:0x7fe2ff|label:S|path=fillcolor:0xAA000033|color:0xFFFFFF00|"
                                        + gc.toString())
                                .get());
            }

        } else {
            $$(PrivateEventDeleteIds.privateEventDeleteNotice).setTextContent(privateEventReturn.returnMsg());
        }

    }//init

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        UCDelete:
        {
            itsNatHTMLDocument__.addEventListener((EventTarget) $$(PrivateEventDeleteIds.privateEventDelete), EventType.CLICK.toString(), new EventListener() {

                final HumanId myhumanId = humanId;
                final Long myprivateEventId = privateEventId;
                final Obj<Boolean> alertedUserWithConfirm = (Obj<Boolean>) new Obj<Boolean>().setObjAsValid(false);

                @Override
                public void handleEvent(final Event evt_) {
                    if (!alertedUserWithConfirm.getObj()) {
                        Loggers.USER.info(myhumanId.getObj() + " clicked delete for private event " + myprivateEventId);
                        alertedUserWithConfirm.setObjAsValid(true);
                        $$(evt_).setAttribute(MarkupTag.IMG.src(),
                                $$(evt_).getAttribute(MarkupTag.IMG.src()).replace("JDelete.png", "JConfirm.png")
                        );
                        //$$(evt_).setTextContent("Confirm Delete!");
                    } else {
                        Loggers.USER.info(myhumanId.getObj() + " clicked delete after confirmation for private event " + myprivateEventId);
                        alertedUserWithConfirm.setObjAsValid(false); //needed because chrome or itsnat seems to be resending the event.
                        final Return<Boolean> r = DB.getHumanCrudPrivateEventLocal(true).dPrivateEvent(myhumanId, myprivateEventId);
                        if (r.returnStatus() == 0) {
                            Loggers.USER.info(myhumanId.getObj() + " deleted private event " + r.returnValue());
                            remove(evt_.getTarget(), EventType.CLICK, this, false);
                            $$sendJS(
                                    JSCodeToSend.redirectPageWithURL("/page/_org")
                            );
                            //clear($$(privateEventDeleteNotice));
                        } else {
                            $$(PrivateEventDeleteIds.privateEventDeleteNotice).setTextContent(r.returnMsg());
                        }
                    }


                }

            }, false);
        }

        final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId);
        this.possibilities = user.getHumansNetPeoples();

        final PrivateEvent privateEvent = privateEventReturn.returnValue();

        final List<HumansPrivateEvent> privateEventOwners = privateEvent.getPrivateEventOwners();
        UCAddRemoveOwners:
        {
            UCFriendsDoNotExistToAddRemove_UCAddRemoveOwners:
            {
                new AdaptableSignup(
                        request,
                        new AdaptableSignupCriteria()
                                .setHumanId(humanId)
                                .setWidgetTitle("Add Managers By Their Email")
                                .setAdaptableSignupCallback(
                                        new AdaptableSignupCallback() {

                                            final String myeventLink = eventLink;
                                            final HumanId myhumanId = humanId;
                                            final long myprivateEventId = privateEvent.getPrivateEventId();
                                            final PrivateEvent mymyprivateEvent = privateEvent;
                                            private boolean successful = false;

                                            @Override
                                            public String afterInvite(final HumanId invitee) {

                                                final Human friend = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(invitee.getHumanId());

                                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddOwnerWithPrivateLocationCheck(myhumanId, myprivateEventId, friend, mymyprivateEvent.getPrivateLocation().getPrivateLocationId());
                                                if (returnVal.returnStatus() == 0) {
                                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(invitee.getHumanId(),
                                                            myhumanId.getObj(),
                                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                                    new HumanId(myhumanId.getHumanId()),
                                                                    invitee.getHumanId(),
                                                                    ElementComposer.compose($$(MarkupTag.A))
                                                                            .$ElementSetHref(myeventLink)
                                                                            .$ElementSetText("I added you as a manager of moment " + returnVal.returnValue().getPrivateEventName())
                                                                            .get()
                                                            ));
                                                    successful = true;
                                                    return "Invited to this Moment and added to your profile!";
                                                } else {
                                                    return "Invited and added to your profile, but could not add to this Moment";
                                                }
                                            }

                                            @Override
                                            public String jsToSend(final HumanId invitee) {
                                                if (successful) {
                                                    return JSCodeToSend.refreshPageIn(2);
                                                } else {
                                                    return "";
                                                }
                                            }
                                        }
                                ),
                        $$(PrivateEventDeleteIds.privateEventDeleteOwners));
            }

            UCFriendsExistToAddRemove_UCAddRemoveOwners:
            {
                new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateEvent>>(
                        request, $$(PrivateEventDeleteIds.privateEventDeleteOwners),
                        user.getHumansNet(),
                        possibilities,
                        privateEventOwners,
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = privateEvent.getPrivateEventId();
                            final String myeventLink = eventLink;

                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {


                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddOwnerWithPrivateLocationCheck(humanId, myprivateEventId, humansFriend, privateEvent.getPrivateLocation().getPrivateLocationId());
                                if (returnVal.returnStatus() == 0) {
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                            humanId.getObj(),
                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                    new HumanId(humanId.getHumanId()),
                                                    humansFriend.getHumanId(),
                                                    ElementComposer.compose($$(MarkupTag.A))
                                                            .$ElementSetHref(myeventLink)
                                                            .$ElementSetText("I added you as a manager of moment " + returnVal.returnValue().getPrivateEventName())
                                                            .get()
                                            ));
                                }
                                return returnVal;

                            }
                        },
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = privateEvent.getPrivateEventId();

                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {


                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventRemoveOwner(humanId, myprivateEventId, humansFriend);
                                if (returnVal.returnStatus() == 0) {
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                            humanId.getObj(),
                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                    new HumanId(humanId.getHumanId()),
                                                    humansFriend.getHumanId(),
                                                    ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has removed you as a manager of moment " + returnVal.returnValue().getPrivateEventName()
                                            ));
                                }
                                return returnVal;

                            }
                        }
                ) {
                };
            }
        }
        UCAddRemoveVisitors:
        {
            final List<HumansPrivateEvent> privateEventViewers = privateEvent.getPrivateEventViewers();

            UCFriendsDoNotExistToAddRemove_UCAddRemoveVisitors:
            {
                new AdaptableSignup(
                        request,
                        new AdaptableSignupCriteria()
                                .setHumanId(humanId)
                                .setWidgetTitle("Invite People By Their Email")
                                .setAdaptableSignupCallback(
                                        new AdaptableSignupCallback() {

                                            final String myeventLink = eventLink;
                                            final HumanId myhumanId = humanId;
                                            final long myprivateEventId = privateEvent.getPrivateEventId();
                                            final PrivateEvent mymyprivateEvent = privateEvent;
                                            private boolean successful = false;

                                            @Override
                                            public String afterInvite(final HumanId invitee) {

                                                final Human friend = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(invitee.getHumanId());

                                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddVisitorWithPrivateLocationCheck(myhumanId, myprivateEventId, friend, mymyprivateEvent.getPrivateLocation().getPrivateLocationId());
                                                if (returnVal.returnStatus() == 0) {
                                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(invitee.getHumanId(),
                                                            myhumanId.getObj(),
                                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                                    new HumanId(myhumanId.getHumanId()),
                                                                    invitee.getHumanId(),
                                                                    ElementComposer.compose($$(MarkupTag.A))
                                                                            .$ElementSetHref(myeventLink)
                                                                            .$ElementSetText("I added you as an attendee of moment " + returnVal.returnValue().getPrivateEventName())
                                                                            .get()
                                                            ));
                                                    successful = true;
                                                    return "Invited to this Moment and added to your profile!";
                                                } else {
                                                    return "Invited and added to your profile, but could not add to this Moment";
                                                }
                                            }

                                            @Override
                                            public String jsToSend(HumanId invitee) {
                                                if (successful) {
                                                    return JSCodeToSend.refreshPageIn(5);
                                                } else {
                                                    return "";
                                                }
                                            }
                                        }
                                ),
                        $$(PrivateEventDeleteIds.privateEventDeleteVisitors));
            }


            UCFriendsExistToAddRemove_UCAddRemoveVisitors:
            {
                new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateEvent>>(
                        request, $$(PrivateEventDeleteIds.privateEventDeleteVisitors),
                        user.getHumansNet(),
                        possibilities,
                        privateEventViewers,
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = privateEvent.getPrivateEventId();
                            final String myeventLink = eventLink;

                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {

                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddVisitorWithPrivateLocationCheck(humanId, myprivateEventId, humansFriend, privateEvent.getPrivateLocation().getPrivateLocationId());
                                if (returnVal.returnStatus() == 0) {
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                            humanId.getObj(),
                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                    new HumanId(humanId.getHumanId()),
                                                    humansFriend.getHumanId(),
                                                    ElementComposer.compose($$(MarkupTag.A))
                                                            .$ElementSetHref(myeventLink)
                                                            .$ElementSetText("I added you as an attendee of moment " + returnVal.returnValue().getPrivateEventName())
                                                            .get()
                                            ));
                                }
                                return returnVal;

                            }
                        },
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = privateEvent.getPrivateEventId();


                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventRemoveVisitor(humanId, myprivateEventId, humansFriend);
                                if (returnVal.returnStatus() == 0) {
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                            humanId.getObj(),
                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                    new HumanId(humanId.getHumanId()),
                                                    humansFriend.getHumanId(),
                                                    ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has removed you as an attendee of moment " + returnVal.returnValue().getPrivateEventName()
                                            ));
                                }
                                return returnVal;

                            }
                        }
                ) {
                };
            }
            UCAddRemoveInvitee:
//INVITEES JUST GET NOTIFICATIONS. AS OF 2011-10-01 THESE GUYS CANNOT VIEW THE EVENT. HENCE IT IS HIDDEN.
            {
                new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateEvent>>(
                        request, $$(PrivateEventDeleteIds.privateEventDeleteInvitees),
                        user.getHumansNet(),
                        possibilities,
                        privateEvent.getPrivateEventInvites(),
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = privateEvent.getPrivateEventId();

                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {

                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventAddInviteWithPrivateLocationCheck(humanId, myprivateEventId, humansFriend, privateEvent.getPrivateLocation().getPrivateLocationId());
                                if (returnVal.returnStatus() == 0) {
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                            humanId.getObj(),
                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                    new HumanId(humanId.getHumanId()),
                                                    humansFriend.getHumanId(),
                                                    ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has invited you to moment " + returnVal.returnValue().getPrivateEventName()
                                            ));
                                }
                                return returnVal;


                            }
                        },
                        new Save<Return<PrivateEvent>>() {

                            final long myprivateEventId = privateEvent.getPrivateEventId();

                            @Override
                            public Return<PrivateEvent> save(final HumanId humanId, final HumansFriend humansFriend) {
                                final Return<PrivateEvent> returnVal = DB.getHumanCrudPrivateEventLocal(true).uPrivateEventRemoveInvite(humanId, myprivateEventId, humansFriend);
                                if (returnVal.returnStatus() == 0) {
                                    SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                            humanId.getObj(),
                                            ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                    new HumanId(humanId.getHumanId()),
                                                    humansFriend.getHumanId(),
                                                    ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has cancelled your invitation of moment " + returnVal.returnValue().getPrivateEventName()
                                            ));
                                }
                                return returnVal;
                            }
                        }
                );
            }
        }
    }
}

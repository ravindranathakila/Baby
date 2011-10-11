package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.HumansFriend;
import ai.ilikeplaces.entities.HumansNetPeople;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
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
abstract public class PrivateLocationDelete extends AbstractWidgetListener {
    private static final String PLD_LIST_ITEM = "PLDListItem";
    private static final String NO_MOMENTS_STARTED_HERE_YET = "No moments started here yet!";
    // ------------------------------ FIELDS ------------------------------

    Return<PrivateLocation> r;
    List<HumansNetPeople> possibilities;


    private HumanId humanId = null;
    private Long privateLocationId = null;

// --------------------------- CONSTRUCTORS ---------------------------

    public PrivateLocationDelete(final ItsNatServletRequest request__, final Element appendToElement__, final String humanId__, final long privateLocationId__) {
        super(request__, Page.PrivateLocationDelete, appendToElement__, humanId__, privateLocationId__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        this.humanId = new HumanId();
        humanId.setObjAsValid((String) initArgs[0]);

        this.privateLocationId = (Long) initArgs[1];

        r = DB.getHumanCrudPrivateLocationLocal(true).dirtyRPrivateLocationAsOwner(humanId, privateLocationId);
        if (r.returnStatus() == 0) {
            $$(privateLocationDeleteName).setTextContent(r.returnValue().getPrivateLocationName());
            $$(privateLocationDeleteInfo).setTextContent(r.returnValue().getPrivateLocationInfo());
            SetEventList:
            {
                for (final PrivateEvent pe : r.returnValue().getPrivateEvents()) {
                    if (DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventIsViewer(humanId, pe.getPrivateEventId()).returnValue()) {
                        $$(privateLocationDeleteEventList).appendChild(
                                ElementComposer.compose($$(MarkupTag.DIV))
                                        .$ElementSetClasses(PLD_LIST_ITEM)
                                        .wrapThis(
                                                ElementComposer.compose($$(MarkupTag.A))
                                                        .$ElementSetText(pe.getPrivateEventName())
                                                        .$ElementSetHref(
                                                                new Parameter(Organize.getURL())
                                                                        .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                                                                        .append(DocOrganizeLocation, r.returnValue().getPrivateLocationId())
                                                                        .append(DocOrganizeEvent, pe.getPrivateEventId())
                                                                        .get()
                                                        )
                                                        .get()
                                        )
                                        .get());
                    }
                }
                if (r.returnValue().getPrivateEvents().size() == 0) {
                    $$(privateLocationDeleteEventList).appendChild(
                            ElementComposer.compose($$(MarkupTag.DIV)).$ElementSetText(NO_MOMENTS_STARTED_HERE_YET)
                                    .get());
                }
            }

            SetLocationMap:
            {
                final GeoCoord gc = new GeoCoord();
                gc.setObj(r.returnValue().getPrivateLocationLatitude() + "," + r.returnValue().getPrivateLocationLongitude());
                gc.validateThrow();

                $$(privateLocationDeleteLocationMap).setAttribute(MarkupTag.IMG.src(),
                        new Parameter("http://maps.google.com/maps/api/staticmap")
                                .append("sensor", "false", true)
                                .append("center", gc.toString())
                                .append("size", "230x250")
                                .append("format", "jpg")
                                .append("markers", "color:0x7fe2ff|label:S|path=fillcolor:0xAA000033|color:0xFFFFFF00|"
                                        + gc.toString())
                                .get());
            }
        } else {
            $$(privateLocationDeleteNotice).setTextContent(r.returnMsg());
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {
        delete:
        {
            itsNatHTMLDocument__.addEventListener((EventTarget) $$(privateLocationDelete), EventType.CLICK.toString(), new EventListener() {
                final HumanId myhumanId = humanId;
                final Long myprivateLocationId = privateLocationId;

                @Override
                public void handleEvent(final Event evt_) {

                    final Return<Boolean> r = DB.getHumanCrudPrivateLocationLocal(true).dPrivateLocation(myhumanId, myprivateLocationId);
                    logger.debug("DELETED. DB REPLY:" + r.returnValue());

                    if (r.returnStatus() == 0) {

                        remove(evt_.getTarget(), EventType.CLICK, this);
                        logger.debug("REMOVED CLICK.");

                        clear($$(privateLocationDeleteNotice));
                    } else {
                        $$(privateLocationDelete).setTextContent(r.returnMsg());
                    }
                }

            }, false, JSCodeToSend.RefreshPage);
        }

        final HumansNetPeople user = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansNetPeople(humanId);
        this.possibilities = user.getHumansNetPeoples();

        AddRemoveOwners:
        {
            new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateLocation>>(
                    request, $$(privateLocationDeleteOwners),
                    user.getHumansNet(),
                    possibilities,
                    r.returnValue().getPrivateLocationOwners(),
                    new Save<Return<PrivateLocation>>() {
                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            final Return<PrivateLocation> returnVal = DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddOwner(humanId, myprivateLocationId, humansFriend);
                            if (returnVal.returnStatus() == 0) {
                                SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                        humanId.getObj(),
                                        ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                new HumanId(humanId.getHumanId()),
                                                humansFriend.getHumanId(),
                                                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has added you as an Owner of " + returnVal.returnValue().getPrivateLocationName()));
                            }
                            return returnVal;
                        }
                    },
                    new Save<Return<PrivateLocation>>() {
                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            final Return<PrivateLocation> returnVal = DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveOwner(humanId, myprivateLocationId, humansFriend);
                            if (returnVal.returnStatus() == 0) {
                                SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                        humanId.getObj(),
                                        ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                new HumanId(humanId.getHumanId()),
                                                humansFriend.getHumanId(),
                                                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has removed you as an Owner of " + returnVal.returnValue().getPrivateLocationName()));
                            }

                            return returnVal;
                        }
                    }
            ) {
            };
        }
        AddRemoveVisitors:
        {
            new MemberHandler<HumansFriend, List<HumansFriend>, Return<PrivateLocation>>(
                    request, $$(privateLocationDeleteVisitors),
                    user.getHumansNet(),
                    possibilities,
                    r.returnValue().getPrivateLocationViewers(),
                    new Save<Return<PrivateLocation>>() {
                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            final Return<PrivateLocation> returnVal = DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationAddVisitor(humanId, myprivateLocationId, humansFriend);
                            if (returnVal.returnStatus() == 0) {
                                SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                        humanId.getObj(),
                                        ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                new HumanId(humanId.getHumanId()),
                                                humansFriend.getHumanId(),
                                                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has added you as a Visitor of " + returnVal.returnValue().getPrivateLocationName()));
                            }
                            return returnVal;
                        }
                    },
                    new Save<Return<PrivateLocation>>() {
                        final long myprivateLocationId = r.returnValue().getPrivateLocationId();

                        @Override
                        public Return<PrivateLocation> save(final HumanId humanId, final HumansFriend humansFriend) {
                            final Return<PrivateLocation> returnVal = DB.getHumanCrudPrivateLocationLocal(true).uPrivateLocationRemoveVisitor(humanId, myprivateLocationId, humansFriend);
                            if (returnVal.returnStatus() == 0) {
                                SendMail.getSendMailLocal().sendAsHTMLAsynchronously(humansFriend.getHumanId(),
                                        humanId.getObj(),
                                        ai.ilikeplaces.logic.Listeners.widgets.UserProperty.getUserPropertyHtmlFor(
                                                new HumanId(humanId.getHumanId()),
                                                humansFriend.getHumanId(),
                                                ai.ilikeplaces.logic.Listeners.widgets.UserProperty.SENDER_NAME + " has removed you as a Visitor of " + returnVal.returnValue().getPrivateLocationName()));
                            }
                            return returnVal;
                        }
                    }
            ) {
            };
        }
    }
}
package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.doc.WARNING;
import ai.ilikeplaces.entities.HumanEqualsFace;
import ai.ilikeplaces.entities.HumanIdFace;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.AlbumManager;
import ai.ilikeplaces.logic.Listeners.widgets.Button;
import ai.ilikeplaces.logic.Listeners.widgets.UserProperty;
import ai.ilikeplaces.logic.Listeners.widgets.WallWidgetPrivateEvent;
import ai.ilikeplaces.logic.Listeners.widgets.people.People;
import ai.ilikeplaces.logic.Listeners.widgets.people.PeopleCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static ai.ilikeplaces.servlets.Controller.Page.*;


/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@OK
abstract public class PrivateEventView extends AbstractWidgetListener {


    final private Logger logger = LoggerFactory.getLogger(PrivateEventView.class.getName());
    private static final String ARROW_RIGHT_GIF = "arrow-right.gif";
    private static final String OWNER = "Owner";
    private static final String VISITOR = "Visitor";
    private static final String INVITEE = "Invitee";
    private static final String TALK = "Get in touch";

    private HumanId humanId = null;

    private Long privateEventId = null;

    public static enum PrivateEventViewIds implements WidgetIds {
        privateEventViewNotice,
        privateEventViewName,
        privateEventViewInfo,
        privateEventViewDelete,
        privateEventViewOwners,
        privateEventViewVisitors,
        privateEventViewInvites,
        privateEventViewLink,
        privateEventViewWall,
        privateEventViewAlbum,
        privateEventViewLocationMap,
    }

    /**
     * @param request__
     * @param appendToElement__
     * @param humanId__
     * @param privateEventId__
     * @param detailedMode__
     */
    public PrivateEventView(final ItsNatServletRequest request__, final Element appendToElement__, final String humanId__, final long privateEventId__, final boolean detailedMode__) {
        super(request__, Page.PrivateEventView, appendToElement__, humanId__, privateEventId__, detailedMode__);
    }

    /**
     *
     */
    @Override
    protected void init(final Object... initArgs) {
        humanId = new HumanId((String) initArgs[0]);
        privateEventId = (Long) initArgs[1];

        @WARNING("Underlying widgets expect a fully refreshed PrivateEvent with respect to owners, visitors and viewers")
        final Return<PrivateEvent> r = DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventAsAny(humanId.getObjectAsValid(), privateEventId);


        LoggerFactory.getLogger(PrivateEventView.class.getName()).debug(r.toString());

        if (r.returnStatus() == 0) {
            $$(PrivateEventViewIds.privateEventViewName).setTextContent(r.returnValue().getPrivateEventName());
            $$(PrivateEventViewIds.privateEventViewInfo).setTextContent(r.returnValue().getPrivateEventInfo());
            new Button(request, $$(PrivateEventViewIds.privateEventViewLink), "Link to " + r.returnValue().getPrivateEventName(), false, r.returnValue()) {
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
                            $$(GenericButtonImage).setAttribute(MarkupTag.IMG.src(), RBGet.globalConfig.getString(RBGet.url_CDN_STATIC) + ARROW_RIGHT_GIF);
                        }
                    }
                }
            };
            if ((Boolean) initArgs[2]) {
                new WallWidgetPrivateEvent(request, $$(PrivateEventViewIds.privateEventViewWall), humanId, r.returnValue().getPrivateEventId());
                new AlbumManager(request, $$(PrivateEventViewIds.privateEventViewAlbum), humanId, r.returnValue());

                final GeoCoord gc = new GeoCoord();
                gc.setObj(r.returnValue().getPrivateLocation().getPrivateLocationLatitude() + "," + r.returnValue().getPrivateLocation().getPrivateLocationLongitude());
                gc.validateThrow();

                $$(PrivateEventViewIds.privateEventViewLocationMap).setAttribute(MarkupTag.IMG.src(),
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

            final List<HumansPrivateEvent> privateEventOwners = r.returnValue().getPrivateEventOwners();
            UCShowOwners:
            {
                for (final HumansPrivateEvent hpe : privateEventOwners) {
                    new UserProperty(request, $$(PrivateEventViewIds.privateEventViewOwners), new HumanId(hpe.getHumanId())) {
                        protected void init(final Object... initArgs) {
                            $$(Controller.Page.user_property_content).appendChild(
                                    ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(TALK).$ElementSetHref(ProfileRedirect.PROFILE_URL + hpe.getHumanId()).get()
                            );
                        }
                    };
                }
            }

            final List<HumansPrivateEvent> privateEventViewers = r.returnValue().getPrivateEventViewers();
            UCShowViewers:
            {
                for (final HumansPrivateEvent hpe : privateEventViewers) {
                    new UserProperty(request, $$(PrivateEventViewIds.privateEventViewVisitors), new HumanId(hpe.getHumanId())) {
                        protected void init(final Object... initArgs) {
                            $$(Page.user_property_content).appendChild(
                                    ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(TALK).$ElementSetHref(ProfileRedirect.PROFILE_URL + hpe.getHumanId()).get()
                            );
                        }
                    };
                }
            }

            UCShowInvites:
            {
                for (final HumansPrivateEvent hpe : r.returnValue().getPrivateEventInvites()) {
                    new UserProperty(request, $$(PrivateEventViewIds.privateEventViewInvites), new HumanId(hpe.getHumanId())) {
                        protected void init(final Object... initArgs) {
                            $$(Controller.Page.user_property_content).setTextContent(INVITEE);
                        }
                    };
                }
            }

            UCFiltering:
            {
                new People(request, new PeopleCriteria().setPeople((List<HumanIdFace>) (List<?>)
                        new ArrayList<HumansPrivateEvent>(new HashSet<HumansPrivateEvent>() {
                            final HumanId myhumanId = humanId;

                            {
                                privateEventOwners.remove(myhumanId);
                                privateEventViewers.remove(myhumanId);
                                addAll(privateEventOwners);
                                addAll(privateEventViewers);
                            }
                        })

                ), $(Controller.Page.Skeleton_left_column));
            }

        } else {
            $$(PrivateEventViewIds.privateEventViewNotice).setTextContent(r.returnMsg());
        }

    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__) {

        delete:
        {
            itsNatHTMLDocument__.addEventListener((EventTarget) $$(PrivateEventViewIds.privateEventViewDelete), EventType.CLICK.toString(), new EventListener() {

                final HumanId myhumanId = humanId;
                final Long myprivateEventId = privateEventId;
                final Obj<Boolean> alertedUserWithConfirm = (Obj<Boolean>) new Obj<Boolean>().setObjAsValid(false);

                @Override
                public void handleEvent(final Event evt_) {
                    if (!alertedUserWithConfirm.getObj()) {
                        Loggers.USER.info(myhumanId.getObj() + " clicked delete for private event " + myprivateEventId);
                        alertedUserWithConfirm.setObjAsValid(true);
                        $$(evt_).setAttribute(MarkupTag.IMG.src(),
                                $$(evt_).getAttribute(MarkupTag.IMG.src()).replace("delete.png", "confirm.png")
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
                            $$(PrivateEventViewIds.privateEventViewNotice).setTextContent(r.returnMsg());
                        }
                    }


                }

            }, false);
        }
    }

}
package ai.ilikeplaces.logic.Listeners.widgets.privateevent;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.OK;
import ai.ilikeplaces.entities.Msg;
import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.*;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.*;
import ai.ilikeplaces.util.cache.SmartCache3;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
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
public class PrivateEventViewSidebar extends AbstractWidgetListener<PrivateEventViewSidebarCriteria> {
// ------------------------------ FIELDS ------------------------------


// ------------------------------ FIELDS STATIC --------------------------

    public static final SmartCache3<Long, PrivateEvent, HumanId> PRIVATE_EVENT_BASIC_INFO_CACHE = new SmartCache3<Long, PrivateEvent, HumanId>(

            new SmartCache3.RecoverWith<Long, PrivateEvent, HumanId>() {
                @Override
                public PrivateEvent getValue(final Long privateEventId, HumanId humanId) {

                    return DB.getHumanCrudPrivateEventLocal(true).dirtyRPrivateEventInfoAsAny(humanId.getObjectAsValid(), privateEventId).returnValueBadly();
                }
            }
    );
    
    private static final String READ_MORE = "read.more";

// --------------------------- CONSTRUCTORS ---------------------------
    public PrivateEventViewSidebar(final ItsNatServletRequest request__, final PrivateEventViewSidebarCriteria privateEventViewSidebarCriteria, final Element appendToElement__) {

        super(request__, Page.PrivateEventViewSidebar, privateEventViewSidebarCriteria, appendToElement__);
    }

// ------------------------ OVERRIDING METHODS ------------------------
    /**
     * @param privateEventViewSidebarCriteria
     *
     */
    @Override
    protected void init(final PrivateEventViewSidebarCriteria privateEventViewSidebarCriteria) {

        final HumanId humanId = new HumanId(privateEventViewSidebarCriteria.getHumanId__());
        final long privateEventId = privateEventViewSidebarCriteria.getPrivateEventId__();


//            final Obj<Long> wallId = (Obj<Long>) new Obj<Long>().setObjAsValid(r.returnValue().getPrivateEventWall().getWallId()).getSelfAsValid();
//
//            final List<Msg> msgs = DB.getHumanCrudPrivateEventLocal(false).readWallLastEntries(
//                    humanId,
//                    wallId,
//                    1,
//                    new RefreshSpec()).returnValue();

        final PrivateEvent privateEvent = PRIVATE_EVENT_BASIC_INFO_CACHE.get(privateEventId, humanId);

        final Msg lastWallEntry = WallWidgetPrivateEvent.LAST_WALL_ENTRY.get(privateEvent.getPrivateEventWall().getWallId(), humanId.getHumanId());

        final String href = new Parameter(Organize.getURL())
                .append(DocOrganizeCategory, DocOrganizeModeEvent, true)
                .append(DocOrganizeLocation, privateEvent.getPrivateLocation().getPrivateLocationId())
                .append(DocOrganizeEvent, privateEvent.getPrivateEventId())
                .get();


        final GeoCoord gc = new GeoCoord();
        gc.setObj(privateEvent.getPrivateLocation().getPrivateLocationLatitude(), privateEvent.getPrivateLocation().getPrivateLocationLongitude());
        gc.validateThrow();
        $$(Page.private_event_view_sidebar_name).setTextContent(privateEvent.getPrivateEventName());

        $$(Page.private_event_view_sidebar_profile_photo).setAttribute(MarkupTag.IMG.title(),
                new Parameter("http://maps.google.com/maps/api/staticmap")
                        .append("sensor", "false", true)
                        .append("center", gc.toString())
                        .append("size", "122x200")
                        .append("format", "jpg")
                        .append("markers", "color:0x7fe2ff|label:S|path=fillcolor:0xAA000033|color:0xFFFFFF00|"
                                + gc.toString())
                        .get());

        if (lastWallEntry != null) {
            new UserPropertySidebar(
                    request,
                    $$(Page.private_event_view_sidebar_content),
                    new HumanId(lastWallEntry.getMsgMetadata()),
                    lastWallEntry,
                    href) {
                private Msg mylastWallEntry;
                private String myhref;
                protected void init(final Object... initArgs) {

                    mylastWallEntry = (Msg) ((Object[]) initArgs[1])[0];
                    myhref = (String) ((Object[]) initArgs[1])[1];

                    $$displayBlock($$(UserPropertySidebarIds.user_property_sidebar_talk));
                    $$displayNone($$(UserPropertySidebarIds.user_property_sidebar_name_section));

                    String msgContent = lastWallEntry.getMsgContent();

                    TrimMessageContentForReadabilityOnSidebar:
                    {
                        final int length = msgContent.length();
                        if (40 < length) {
                            msgContent = msgContent.substring(0, 40) + RBGet.gui().getString(READ_MORE);
                        }
                    }

                    Element commentHref = ElementComposer.compose($$(MarkupTag.A)).$ElementSetText(msgContent).$ElementSetHref(myhref).get();
                    $$(UserPropertySidebarIds.user_property_sidebar_content).appendChild(commentHref);
                }

                @Override
                protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

                    itsNatHTMLDocument_.addEventListener((EventTarget) $$(UserPropertySidebarIds.user_property_sidebar_engage), EventType.CLICK.toString(), new EventListener() {
                        @Override
                        public void handleEvent(final Event evt_) {

                            $$sendJS(JSCodeToSend.redirectPageWithURL(myhref));
                        }
                    }, false);
                }
            };
        } else {
            new UserPropertySidebar(
                    request,
                    $$(Page.private_event_view_sidebar_content),
                    humanId,
                    "",
                    href) {
                private String myhref;
                protected void init(final Object... initArgs) {

                    myhref = (String) ((Object[]) initArgs[1])[1];

                    $$displayBlock($$(UserPropertySidebarIds.user_property_sidebar_talk));
                    $$displayNone($$(UserPropertySidebarIds.user_property_sidebar_name_section));

                    Element commentHref = ElementComposer.compose($$(MarkupTag.A)).$ElementSetText("").$ElementSetHref(myhref).get();
                    $$(UserPropertySidebarIds.user_property_sidebar_content).appendChild(commentHref);
                }

                @Override
                protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

                    itsNatHTMLDocument_.addEventListener((EventTarget) $$(UserPropertySidebarIds.user_property_sidebar_engage), EventType.CLICK.toString(), new EventListener() {
                        @Override
                        public void handleEvent(final Event evt_) {

                            $$sendJS(JSCodeToSend.redirectPageWithURL(myhref));
                        }
                    }, false);
                }
            };
        }
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {

    }
}
